package com.inventory_backend.inventory_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private String unit; // KG or PCS

    private String description;

    private Double prefixPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<SupplierProductPrice> supplierPrices;

}
