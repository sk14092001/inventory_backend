package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseDetailsResponse {
    private Long productId;
    private String productName;
    private BigDecimal qty;
    private BigDecimal price;
    private BigDecimal lineTotal;
}
