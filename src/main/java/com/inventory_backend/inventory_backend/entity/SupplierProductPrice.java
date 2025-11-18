package com.inventory_backend.inventory_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "supplier_product_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Double price;

    private LocalDate validFrom;
    private LocalDate validTo;    // NULL = active
}
