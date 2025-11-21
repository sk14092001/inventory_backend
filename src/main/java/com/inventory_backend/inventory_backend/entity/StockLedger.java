package com.inventory_backend.inventory_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock_ledger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockLedgerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    private String transactionType;

    private Long transactionId;

    private Double qtyIn;
    private Double qtyOut;

    private Double closingStock;

    private LocalDate transactionDate;

    private String remarks;
}
