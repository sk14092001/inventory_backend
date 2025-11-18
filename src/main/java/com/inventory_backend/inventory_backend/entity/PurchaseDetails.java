package com.inventory_backend.inventory_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(precision = 19, scale = 4)
    private BigDecimal qty;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @Column(precision = 19, scale = 2)
    private BigDecimal total;
}
