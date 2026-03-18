package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.*;
import com.inventory_backend.inventory_backend.entity.*;
import com.inventory_backend.inventory_backend.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepo;
    private final PurchaseDetailsRepository purchaseDetailsRepo;
    private final SupplierProductPriceRepository supplierPriceRepo;
    private final SupplierAdvanceRepository advanceRepo;
    private final SupplierAdvanceLedgerRepository ledgerRepo;
    private final ProductRepository productRepo;
    private final StockService stockService;
    private final SupplierRepository supplierRepository;

    private static final int PAGE_SIZE = 200;


    public PurchaseTotalResponse calculateTotal(PurchaseRequest request, Supplier supplier) {

        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseItemDto item : request.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            SupplierProductPrice priceInfo = supplierPriceRepo
                    .findBySupplierAndProduct(supplier.getSupplierId(), product.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Supplier price not found for supplierId = "
                                    + supplier.getSupplierId() + " and productId = " + product.getProductId()
                    ));

            BigDecimal price = BigDecimal.valueOf(priceInfo.getPrice());
            total = total.add(price.multiply(item.getQty()));
        }

        return new PurchaseTotalResponse(total, BigDecimal.ZERO, total);
    }


    @Transactional
    public PurchaseCreateResponse createPurchase(PurchaseRequest request,
                                                 Supplier supplier,
                                                 BigDecimal paidAmount) {

        if (paidAmount == null) paidAmount = BigDecimal.ZERO;

        PurchaseTotalResponse calc = calculateTotal(request, supplier);

        Purchase purchase = savePurchaseHeader(request, supplier, calc);

        List<PurchaseDetailsResponse> detailsResponses = new ArrayList<>();

        for (PurchaseItemDto item : request.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            SupplierProductPrice priceInfo = supplierPriceRepo
                    . findBySupplierAndProduct(supplier.getSupplierId(), product.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Supplier price not found for Supplier ID: "
                                    + supplier.getSupplierId() + " and Product ID: "
                                    + product.getProductId()
                    ));

            BigDecimal price = BigDecimal.valueOf(priceInfo.getPrice());
            BigDecimal lineTotal = price.multiply(item.getQty());

            PurchaseDetails details = PurchaseDetails.builder()
                    .purchase(purchase)
                    .product(product)
                    .qty(item.getQty())
                    .price(price)
                    .total(lineTotal)
                    .build();

            details = purchaseDetailsRepo.save(details);

            stockService.stockIn(product, details.getPurchaseDetailId(), item.getQty().doubleValue());

            detailsResponses.add(new PurchaseDetailsResponse(
                    product.getProductId(),
                    product.getName(),
                    item.getQty(),
                    price,
                    lineTotal
            ));
        }


        if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            addAdvance(supplier, paidAmount, "PURCHASE_PAYMENT", purchase.getPurchaseId());
        }

        deductAdvanceFIFO(supplier, purchase, calc.getGrandTotal());

        return new PurchaseCreateResponse(
                "SUCCESS",
                "Purchase created successfully",
                purchase.getPurchaseId(),
                calc.getTotalAmount(),
                calc.getDiscount(),
                calc.getGrandTotal(),
                detailsResponses
        );
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

    @Transactional
    private void savePurchaseDetails(PurchaseRequest req,
                                     Supplier supplier,
                                     Purchase purchase) {

        for (PurchaseItemDto item : req.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            SupplierProductPrice priceInfo =
                    supplierPriceRepo.findBySupplierAndProduct(supplier.getSupplierId(), product.getProductId())
                            .orElseThrow(() -> new RuntimeException("Supplier product price not found"));

            BigDecimal price = BigDecimal.valueOf(priceInfo.getPrice());
            BigDecimal lineTotal = price.multiply(item.getQty());

            PurchaseDetails details = PurchaseDetails.builder()
                    .purchase(purchase)
                    .product(product)
                    .qty(item.getQty())
                    .price(price)
                    .total(lineTotal)
                    .build();

            details = purchaseDetailsRepo.save(details);

            stockService.stockIn(
                    product,
                    details.getPurchaseDetailId(),
                    item.getQty().doubleValue()
            );
        }
    }


    @Transactional
    public SupplierAdvanceLedger addAdvance(Supplier supplier,
                                            BigDecimal amount,
                                            String referenceType,
                                            Long referenceId) {


        SupplierAdvance advance = SupplierAdvance.builder()
                .supplier(supplier)
                .originalAmount(amount)
                .amountRemaining(amount)
                .advanceDate(LocalDate.now())
                .remarks(referenceType)
                .build();

        advance = advanceRepo.save(advance);

        BigDecimal lastBalance = Optional.ofNullable(
                ledgerRepo.getLastBalance(supplier.getSupplierId())
        ).orElse(BigDecimal.ZERO);

        BigDecimal newBalance = lastBalance.add(amount);

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


    @Transactional
    public SupplierAdvanceLedger payWithoutPurchase(Supplier supplier, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be > 0");

        return addAdvance(supplier, amount, "DIRECT_PAYMENT", null);
    }


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

    public SupplierPaymentResponse payOnly(Supplier supplier, BigDecimal amount) {

        SupplierAdvanceLedger ledger = addStandaloneAdvancePayment(supplier, amount);

        return new SupplierPaymentResponse(
                "SUCCESS",
                "Payment recorded successfully",
                amount,
                ledger.getBalanceAfterTransaction(),
                ledger.getLedgerId()
        );
    }

    @Transactional
    public SupplierAdvanceLedger addStandaloneAdvancePayment(Supplier supplier, BigDecimal amount) {

        SupplierAdvance adv = SupplierAdvance.builder()
                .supplier(supplier)
                .originalAmount(amount)
                .amountRemaining(amount)
                .advanceDate(LocalDate.now())
                .remarks("PAY_ONLY")
                .build();
        adv = advanceRepo.save(adv);

        BigDecimal lastBalance = ledgerRepo.getLastBalance(supplier.getSupplierId());
        if (lastBalance == null) lastBalance = BigDecimal.ZERO;

        BigDecimal newBalance = lastBalance.add(amount);

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

    public SupplierBalanceResponse getSupplierBalanceDetails(Long supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        BigDecimal balance = ledgerRepo.getLastBalance(supplierId);
        if (balance == null) balance = BigDecimal.ZERO;

        return new SupplierBalanceResponse(
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress(),
                balance
        );
    }

    public SupplierLedgerResponse getSupplierLedger(Long supplierId,LocalDate start,LocalDate end) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (start==null) start=LocalDate.of(1970,1,1);
        if(end==null) end=LocalDate.now();

        List<SupplierAdvanceLedger> ledgerList =
                ledgerRepo.findBySupplierAndDateRange(supplierId, start, end);

        BigDecimal balance = ledgerRepo.getLastBalance(supplierId);
        if (balance == null) balance = BigDecimal.ZERO;


        SupplierResponseDTO supplierDTO = new SupplierResponseDTO(
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress(),
                supplier.getCreatedAt()
        );


        List<SupplierLedgerDTO> ledgerDtoList = ledgerList.stream()
                .map(l -> new SupplierLedgerDTO(
                        l.getLedgerId(),
                        l.getTransactionType(),
                        l.getAmount(),
                        l.getBalanceAfterTransaction(),
                        l.getTransactionDate(),
                        l.getReferenceType(),
                        l.getReferenceId(),
                        l.getRemarks()
                ))
                .toList();

        return new SupplierLedgerResponse(supplierDTO, balance, ledgerDtoList);

    }

    public byte[] getSupplierLedgerPdf(Long supplierId, LocalDate start, LocalDate end) {

        SupplierLedgerResponse ledgerResponse=getSupplierLedger(supplierId,start,end);
        try{
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont=new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title=new Paragraph("SupplierLedger",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph());
        document.add(new Paragraph("Supplier Id :"+ledgerResponse.getSupplier().getSupplierId()));
        document.add(new Paragraph("Supplier Name:"+ledgerResponse.getSupplier().getName()));
        document.add(new Paragraph("Phone :"+ledgerResponse.getSupplier().getPhone()));
        document.add(new Paragraph("Email:"+ledgerResponse.getSupplier().getEmail()));
        document.add(new Paragraph());

           PdfPTable table=new PdfPTable(6);
           table.setWidthPercentage(100);
            table.addCell("Date");
            table.addCell("Type");
            table.addCell("Amount");
            table.addCell("Balance After");
            table.addCell("Reference Type");
            table.addCell("Reference ID");

            for (SupplierLedgerDTO entry:ledgerResponse.getLedger())
            {
                table.addCell(entry.getTransactionDate().toString());
                table.addCell(entry.getTransactionType());
                table.addCell(entry.getAmount().toString());
                table.addCell(entry.getBalanceAfterTransaction().toString());
                table.addCell(entry.getReferenceType());
                table.addCell(entry.getReferenceId().toString());

            }
            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Error Generating Pdf"+e.getMessage());
        }
    }
}