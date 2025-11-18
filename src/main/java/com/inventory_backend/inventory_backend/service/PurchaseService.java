package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.PurchaseItemDto;
import com.inventory_backend.inventory_backend.dto.PurchaseRequest;
import com.inventory_backend.inventory_backend.dto.PurchaseTotalResponse;
import com.inventory_backend.inventory_backend.entity.*;
import com.inventory_backend.inventory_backend.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepo;
    private final PurchaseDetailsRepository purchaseDetailsRepo;
    private final SupplierProductPriceRepository supplierPriceRepo;
    private final SupplierAdvanceRepository advanceRepo;
    private final SupplierAdvanceLedgerRepository ledgerRepo;
    private final ProductRepository productRepo;

    private static final int PAGE_SIZE = 200;

    /* ----------------------------------------------------------------------
       1. CALCULATE TOTAL
    ---------------------------------------------------------------------- */
    public PurchaseTotalResponse calculateTotal(PurchaseRequest request, Supplier supplier) {

        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseItemDto item : request.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            SupplierProductPrice priceInfo =
                    supplierPriceRepo.findBySupplierAndProduct(supplier, product)
                            .orElseThrow(() -> new RuntimeException("Supplier price not found"));

            BigDecimal price = BigDecimal.valueOf(priceInfo.getPrice());
            total = total.add(price.multiply(item.getQty()));
        }

        return new PurchaseTotalResponse(total, BigDecimal.ZERO, total);
    }

    /* ----------------------------------------------------------------------
       2. CREATE PURCHASE (WITH OPTIONAL IMMEDIATE PAYMENT)
    ---------------------------------------------------------------------- */
    @Transactional
    public Purchase createPurchase(PurchaseRequest request,
                                   Supplier supplier,
                                   BigDecimal paidAmount) {

        if (paidAmount == null) paidAmount = BigDecimal.ZERO;

        // Step A — calculate final total
        PurchaseTotalResponse totalRes = calculateTotal(request, supplier);
        BigDecimal grandTotal = totalRes.getGrandTotal();

        // Step B — create purchase header
        Purchase purchase = savePurchaseHeader(request, supplier, totalRes);

        // Step C — save purchase item details
        savePurchaseDetails(request, supplier, purchase);

        // Step D — if paid during creation → add advance (CR)
        if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            addAdvance(supplier, paidAmount, "PURCHASE_PAYMENT", purchase.getPurchaseId());
        }

        // Step E — auto deduct via FIFO
        deductAdvanceFIFO(supplier, purchase, grandTotal);

        return purchase;
    }

    private Purchase savePurchaseHeader(PurchaseRequest req, Supplier supplier, PurchaseTotalResponse calc) {

        Purchase purchase = Purchase.builder()
                .supplier(supplier)
                .invoiceNo(req.getInvoiceNo())
                .invoiceDate(LocalDate.now())
                .totalAmount(calc.getTotalAmount())
                .discount(calc.getDiscount())
                .grandTotal(calc.getGrandTotal())
                .build();

        return purchaseRepo.save(purchase);
    }

    private void savePurchaseDetails(PurchaseRequest req, Supplier supplier, Purchase purchase) {

        List<PurchaseDetails> list = new ArrayList<>();

        for (PurchaseItemDto item : req.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            SupplierProductPrice priceInfo =
                    supplierPriceRepo.findBySupplierAndProduct(supplier, product)
                            .orElseThrow(() -> new RuntimeException("Supplier product price not found"));

            BigDecimal price = BigDecimal.valueOf(priceInfo.getPrice());
            BigDecimal lineTotal = price.multiply(item.getQty());

            list.add(
                    PurchaseDetails.builder()
                            .purchase(purchase)
                            .product(product)
                            .qty(item.getQty())
                            .price(price)
                            .total(lineTotal)
                            .build()
            );
        }

        purchaseDetailsRepo.saveAll(list);
    }


    /* ----------------------------------------------------------------------
       3. ADD ADVANCE PAYMENT (CREDIT)
       -> Used in: PURCHASE_PAYMENT & DIRECT_PAYMENT
    ---------------------------------------------------------------------- */
    @Transactional
    public SupplierAdvanceLedger addAdvance(Supplier supplier,
                                            BigDecimal amount,
                                            String referenceType,
                                            Long referenceId) {

        // 1. Create advance bucket
        SupplierAdvance advance = SupplierAdvance.builder()
                .supplier(supplier)
                .originalAmount(amount)
                .amountRemaining(amount)
                .advanceDate(LocalDate.now())
                .remarks(referenceType)
                .build();

        advance = advanceRepo.save(advance);

        // 2. Calculate new ledger balance
        BigDecimal lastBalance = Optional.ofNullable(
                ledgerRepo.getLastBalance(supplier.getSupplierId())
        ).orElse(BigDecimal.ZERO);

        BigDecimal newBalance = lastBalance.add(amount);

        // 3. Create ledger entry
        SupplierAdvanceLedger ledgerEntry = SupplierAdvanceLedger.builder()
                .advance(advance)
                .supplier(supplier)
                .transactionType("CR")
                .amount(amount)
                .balanceAfterTransaction(newBalance)
                .transactionDate(LocalDate.now())
                .referenceType(referenceType)
                .referenceId(referenceId)
                .remarks("Advance added")
                .createdAt(LocalDateTime.now())
                .build();

        return ledgerRepo.save(ledgerEntry);
    }


    /* ----------------------------------------------------------------------
       4. PAYMENT WITHOUT PURCHASE (Scenario 3)
    ---------------------------------------------------------------------- */
    @Transactional
    public SupplierAdvanceLedger payWithoutPurchase(Supplier supplier, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be > 0");

        return addAdvance(supplier, amount, "DIRECT_PAYMENT", null);
    }


    /* ----------------------------------------------------------------------
       5. FIFO ADVANCE DEDUCTION (Purchase)
    ---------------------------------------------------------------------- */
    private void deductAdvanceFIFO(Supplier supplier,
                                   Purchase purchase,
                                   BigDecimal grandTotal) {

        BigDecimal remaining = grandTotal;
        BigDecimal ledgerBalance = Optional.ofNullable(
                ledgerRepo.getLastBalance(supplier.getSupplierId())
        ).orElse(BigDecimal.ZERO);

        int page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        while (remaining.compareTo(BigDecimal.ZERO) > 0) {

            Page<SupplierAdvance> advances =
                    advanceRepo.findBySupplierAndAmountRemainingGreaterThanOrderByAdvanceDateAsc(
                            supplier, BigDecimal.ZERO, pageable);

            if (!advances.hasContent())
                break;

            for (SupplierAdvance adv : advances.getContent()) {

                if (remaining.compareTo(BigDecimal.ZERO) <= 0)
                    break;

                BigDecimal available = adv.getAmountRemaining();
                BigDecimal deduct = available.min(remaining);

                adv.setAmountRemaining(available.subtract(deduct));
                advanceRepo.save(adv);

                BigDecimal newBalance = ledgerBalance.subtract(deduct);

                ledgerRepo.save(
                        SupplierAdvanceLedger.builder()
                                .advance(adv)
                                .supplier(supplier)
                                .transactionType("DB")
                                .amount(deduct)
                                .balanceAfterTransaction(newBalance)
                                .transactionDate(LocalDate.now())
                                .referenceType("PURCHASE")
                                .referenceId(purchase.getPurchaseId())
                                .remarks("Auto deducted for purchase")
                                .createdAt(LocalDateTime.now())
                                .build()
                );

                ledgerBalance = newBalance;
                remaining = remaining.subtract(deduct);
            }

            if (!advances.hasNext()) break;
            pageable = PageRequest.of(++page, PAGE_SIZE);
        }

        // if advance fully consumed but purchase still pending → record pending debit
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal newBalance = ledgerBalance.subtract(remaining);

            ledgerRepo.save(
                    SupplierAdvanceLedger.builder()
                            .advance(null)
                            .supplier(supplier)
                            .transactionType("DB")
                            .amount(remaining)
                            .balanceAfterTransaction(newBalance)
                            .transactionDate(LocalDate.now())
                            .referenceType("PURCHASE_PENDING")
                            .referenceId(purchase.getPurchaseId())
                            .remarks("Pending amount after exhausting advances")
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
    }

    @Transactional
    public SupplierAdvanceLedger addStandaloneAdvancePayment(Supplier supplier, BigDecimal amount) {

        // Create advance
        SupplierAdvance adv = SupplierAdvance.builder()
                .supplier(supplier)
                .originalAmount(amount)
                .amountRemaining(amount)
                .advanceDate(LocalDate.now())
                .remarks("PAY_ONLY")
                .build();
        adv = advanceRepo.save(adv);

        // Get last balance
        BigDecimal lastBalance = ledgerRepo.getLastBalance(supplier.getSupplierId());
        if (lastBalance == null) lastBalance = BigDecimal.ZERO;

        BigDecimal newBalance = lastBalance.add(amount);

        // Ledger entry
        SupplierAdvanceLedger ledger = SupplierAdvanceLedger.builder()
                .advance(adv)
                .supplier(supplier)
                .transactionType("CR")
                .amount(amount)
                .balanceAfterTransaction(newBalance)
                .transactionDate(LocalDate.now())
                .referenceType("PAY_ONLY")
                .referenceId(null)
                .remarks("Advance added without purchase")
                .createdAt(LocalDateTime.now())
                .build();

        return ledgerRepo.save(ledger);
    }


}
