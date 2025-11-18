package com.inventory_backend.inventory_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_advance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SupplierAdvance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    // original amount paid by supplier
    @Column(precision = 19, scale = 2)
    private BigDecimal originalAmount;

    // remaining amount available to deduct
    @Column(precision = 19, scale = 2)
    private BigDecimal amountRemaining;

    private LocalDate advanceDate;
    private String remarks;
    private LocalDateTime createdAt = LocalDateTime.now();
}
