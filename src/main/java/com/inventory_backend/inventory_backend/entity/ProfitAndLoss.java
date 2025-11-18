package com.inventory_backend.inventory_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "profit_and_loss")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfitAndLoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plId;

    private String periodType;           // DAILY | WEEKLY | MONTHLY | YEARLY
    private LocalDate periodStart;
    private LocalDate periodEnd;

    private Double totalPurchase;
    private Double totalSales;
    private Double totalProfit;

    private LocalDateTime createdAt = LocalDateTime.now();
}
