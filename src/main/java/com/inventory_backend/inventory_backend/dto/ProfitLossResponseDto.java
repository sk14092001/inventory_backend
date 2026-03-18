package com.inventory_backend.inventory_backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class ProfitLossResponseDto {

    private Long supplierId;
    private String supplierName;

    private BigDecimal totalPurchase;
    private BigDecimal totalSales;
    private BigDecimal profit;

    private String periodType;
}
