package com.inventory_backend.inventory_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerLedger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private CustomerPayment payment;

    private String transactionType;

    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;

    private LocalDate transactionDate;

    private String referenceType;  // SALE / SALE_PENDING / PAYMENT_ONLY
    private Long referenceId;

    private String remarks;

    private LocalDateTime createdAt;


}
