package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.results.graph.collection.internal.BagInitializer;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitLossResponse {

    private Long supplierId;
    private String supplierName;

    private BigDecimal totalPurchase;
    private BigDecimal totalSales;
    private BigDecimal profit;

    private String periodType;
    private LocalDate periodStart;
    private LocalDate periodEnd;


    public ProfitLossResponse(BigDecimal totalPurchase,
                              BigDecimal totalSales,
                              BigDecimal profit,
                              String periodType,
                              LocalDate periodStart,
                              LocalDate periodEnd) {

        this.totalPurchase = totalPurchase;
        this.totalSales = totalSales;
        this.profit = profit;
        this.periodType = periodType;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }
}
