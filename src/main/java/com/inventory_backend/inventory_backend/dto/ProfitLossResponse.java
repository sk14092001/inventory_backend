package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitLossResponse {

    private Long supplierId;
    private String supplierName;

    private double totalPurchase;
    private double totalSales;
    private double profit;

    private String periodType;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    // Constructor used by service when supplier name is not required
    public ProfitLossResponse(double totalPurchase,
                              double totalSales,
                              double profit,
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
