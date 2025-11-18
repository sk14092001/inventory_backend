package com.inventory_backend.inventory_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Purchase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private String invoiceNo;
    private LocalDate invoiceDate;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal discount;

    @Column(precision = 19, scale = 2)
    private BigDecimal grandTotal;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseDetails> details;
}
