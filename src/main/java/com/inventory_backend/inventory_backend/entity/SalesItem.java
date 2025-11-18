package com.inventory_backend.inventory_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "sales_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    private Sales sales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Double qty;
    private Double sellingPrice;
    private Double total;
}
