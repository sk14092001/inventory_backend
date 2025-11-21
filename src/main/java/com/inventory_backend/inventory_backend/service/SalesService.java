package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.*;
import com.inventory_backend.inventory_backend.entity.*;
import com.inventory_backend.inventory_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepo;
    private final SalesItemRepository salesItemRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;

    private final CustomerPaymentRepository paymentRepo;
    private final CustomerLedgerRepository ledgerRepo;

    private final StockService stockService;

    private static final int PAGE_SIZE = 200;


    public SalesTotalResponse calculateTotal(SalesRequest request) {

        BigDecimal total = request.getItems().stream()
                .map(item -> item.getSellingPrice().multiply(item.getQty()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO);
        BigDecimal grandTotal = total.subtract(discount);

        return new SalesTotalResponse(total, discount, grandTotal);
    }


    @Transactional
    public SalesCreateResponse createSale(SalesRequest request, Customer customer, BigDecimal paidAmount) {

        if (paidAmount == null) paidAmount = BigDecimal.ZERO;

        SalesTotalResponse totals = calculateTotal(request);


        Sales sale = saveSaleHeader(request, customer, totals);


        List<SalesItemResponse> itemResponses = saveSaleItems(request, sale);


        salesItemRepo.findBySales(sale)
                .forEach(item -> stockService.stockOut(
                        item.getProduct(), item.getSalesItemId(), item.getQty().doubleValue()
                ));


        if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            addPayment(customer, paidAmount, "SALE_PAYMENT", sale.getSalesId());
        }


        deductPaymentFIFO(customer, sale, totals.getGrandTotal());

        return new SalesCreateResponse(
                "SUCCESS",
                "Sale created successfully",
                sale.getSalesId(),
                totals.getTotalAmount(),
                totals.getDiscount(),
                totals.getGrandTotal(),
                itemResponses
        );
    }


    private Sales saveSaleHeader(SalesRequest req, Customer customer, SalesTotalResponse totals) {

        Sales sale = Sales.builder()
                .customer(customer)
                .invoiceNo(req.getInvoiceNo())
                .invoiceDate(LocalDate.now())
                .totalAmount(totals.getTotalAmount())
                .discount(totals.getDiscount())
                .grandTotal(totals.getGrandTotal())
                .build();

        return salesRepo.save(sale);
    }

    @Transactional
    private List<SalesItemResponse> saveSaleItems(SalesRequest req, Sales sale) {

        if (req.getItems() == null) {
            return new ArrayList<>(); // prevent null pointer
        }

        List<SalesItemResponse> list = new ArrayList<>();
        for (SalesItemRequest item : req.getItems()) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal lineTotal = item.getQty().multiply(item.getSellingPrice());

            SalesItem si = SalesItem.builder()
                    .sales(sale)
                    .product(product)
                    .qty(item.getQty())
                    .sellingPrice(item.getSellingPrice())
                    .total(lineTotal)
                    .build();

            salesItemRepo.save(si);

            list.add(new SalesItemResponse(
                    product.getProductId(),
                    product.getName(),
                    item.getQty(),
                    item.getSellingPrice(),
                    lineTotal
            ));
        }
        return list;
    }

    @Transactional
    public CustomerLedger addPayment(Customer customer, BigDecimal amount,
                                     String referenceType, Long referenceId) {

        CustomerPayment cp = CustomerPayment.builder()
                .customer(customer)
                .originalAmount(amount)
                .amountRemaining(amount)
                .paymentDate(LocalDate.now())
                .remarks(referenceType)
                .build();

        cp = paymentRepo.save(cp);

        BigDecimal lastBalance = Optional.ofNullable(ledgerRepo.getLastBalance(customer.getCustomerId()))
                .orElse(BigDecimal.ZERO);

        BigDecimal newBalance = lastBalance.add(amount);

        CustomerLedger ledger = CustomerLedger.builder()
                .customer(customer)
                .payment(cp)
                .transactionType("CR")
                .amount(amount)
                .balanceAfterTransaction(newBalance)
                .transactionDate(LocalDate.now())
                .referenceType(referenceType)
                .referenceId(referenceId)
                .remarks("Payment received")
                .createdAt(LocalDateTime.now())
                .build();

        return ledgerRepo.save(ledger);
    }


    @Transactional
    public void deductPaymentFIFO(Customer customer, Sales sale, BigDecimal grandTotal) {

        BigDecimal remaining = grandTotal;

        BigDecimal ledgerBalance = Optional.ofNullable(
                ledgerRepo.getLastBalance(customer.getCustomerId())
        ).orElse(BigDecimal.ZERO);

        int page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        while (remaining.compareTo(BigDecimal.ZERO) > 0) {

            Page<CustomerPayment> payments =
                    paymentRepo.findByCustomerAndAmountRemainingGreaterThanOrderByPaymentDateAsc(
                            customer, BigDecimal.ZERO, pageable
                    );

            if (!payments.hasContent()) break;

            for (CustomerPayment p : payments.getContent()) {

                if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

                BigDecimal available = p.getAmountRemaining();
                BigDecimal deduct = available.min(remaining);

                p.setAmountRemaining(available.subtract(deduct));
                paymentRepo.save(p);

                BigDecimal newBalance = ledgerBalance.subtract(deduct);

                ledgerRepo.save(
                        CustomerLedger.builder()
                                .customer(customer)
                                .payment(p)
                                .transactionType("DB")
                                .amount(deduct)
                                .balanceAfterTransaction(newBalance)
                                .transactionDate(LocalDate.now())
                                .referenceType("SALE")
                                .referenceId(sale.getSalesId())
                                .remarks("Adjusted against sale")
                                .createdAt(LocalDateTime.now())
                                .build()
                );

                ledgerBalance = newBalance;
                remaining = remaining.subtract(deduct);
            }

            if (!payments.hasNext()) break;

            pageable = PageRequest.of(++page, PAGE_SIZE);
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal newBalance = ledgerBalance.subtract(remaining);

            ledgerRepo.save(
                    CustomerLedger.builder()
                            .customer(customer)
                            .payment(null)
                            .transactionType("DB")
                            .amount(remaining)
                            .balanceAfterTransaction(newBalance)
                            .transactionDate(LocalDate.now())
                            .referenceType("SALE_PENDING")
                            .referenceId(sale.getSalesId())
                            .remarks("Pending after exhausting payments")
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
    }


    public CustomerPaymentResponse payOnly(Customer customer, BigDecimal amount) {

        CustomerLedger ledger = addStandalonePayment(customer, amount);

        return new CustomerPaymentResponse(
                "SUCCESS",
                "Payment added successfully",
                amount,
                ledger.getBalanceAfterTransaction(),
                ledger.getLedgerId()
        );
    }


    @Transactional
    public CustomerLedger addStandalonePayment(Customer customer, BigDecimal amount) {

        CustomerPayment cp = CustomerPayment.builder()
                .customer(customer)
                .originalAmount(amount)
                .amountRemaining(amount)
                .paymentDate(LocalDate.now())
                .remarks("PAY_ONLY")
                .build();

        cp = paymentRepo.save(cp);

        BigDecimal lastBalance = Optional.ofNullable(ledgerRepo.getLastBalance(customer.getCustomerId()))
                .orElse(BigDecimal.ZERO);

        BigDecimal newBalance = lastBalance.add(amount);

        CustomerLedger ledger = CustomerLedger.builder()
                .customer(customer)
                .payment(cp)
                .transactionType("CR")
                .amount(amount)
                .balanceAfterTransaction(newBalance)
                .transactionDate(LocalDate.now())
                .referenceType("PAY_ONLY")
                .remarks("Payment added without sale")
                .createdAt(LocalDateTime.now())
                .build();

        return ledgerRepo.save(ledger);
    }


    public CustomerBalanceResponse getCustomerBalanceDetails(Long customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        BigDecimal balance = Optional.ofNullable(ledgerRepo.getLastBalance(customerId))
                .orElse(BigDecimal.ZERO);

        return new CustomerBalanceResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                balance
        );
    }

    public CustomerLedgerResponse getCustomerLedger(Long customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<CustomerLedger> ledgerList =
                ledgerRepo.findByCustomer_CustomerIdOrderByTransactionDateAsc(customerId);

        BigDecimal balance = Optional.ofNullable(ledgerRepo.getLastBalance(customerId))
                .orElse(BigDecimal.ZERO);

        List<CustomerLedgerDTO> list = ledgerList.stream()
                .map(l -> new CustomerLedgerDTO(
                        l.getLedgerId(),
                        l.getTransactionType(),
                        l.getAmount(),
                        l.getBalanceAfterTransaction(),
                        l.getTransactionDate(),
                        l.getReferenceType(),
                        l.getReferenceId(),
                        l.getRemarks()
                )).toList();

        CustomerResponse customerDTO = new CustomerResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getCreatedAt()
        );

        return new CustomerLedgerResponse(customerDTO, balance, list);
    }
}
