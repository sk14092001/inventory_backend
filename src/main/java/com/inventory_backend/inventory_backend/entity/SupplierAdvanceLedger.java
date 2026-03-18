package com.inventory_backend.inventory_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_advance_ledger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierAdvanceLedger {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advance_id")
    private SupplierAdvance advance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    // "CR" = credit (advance added), "DB" = debit (advance consumed)
    private String transactionType;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(precision = 19, scale = 2)
    private BigDecimal balanceAfterTransaction;

    private LocalDate transactionDate;

    private String referenceType;  // PURCHASE / PAYMENT / MANUAL / PURCHASE_PENDING
    private Long referenceId;

    private String remarks;
    private LocalDateTime createdAt = LocalDateTime.now();
}
