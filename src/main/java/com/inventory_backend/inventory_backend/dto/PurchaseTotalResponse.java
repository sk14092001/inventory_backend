package com.inventory_backend.inventory_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseTotalResponse {
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal grandTotal;

}