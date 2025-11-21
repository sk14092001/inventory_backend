package com.inventory_backend.inventory_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SalesItemResponse {

    private Long productId;
    private String productName;
    private BigDecimal qty;
    private BigDecimal sellingPrice;
    private BigDecimal total;
}
