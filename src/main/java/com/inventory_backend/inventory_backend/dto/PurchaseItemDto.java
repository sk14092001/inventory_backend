package com.inventory_backend.inventory_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseItemDto {
    private Long productId;
    private BigDecimal  qty;
    private BigDecimal price; // unit price
}