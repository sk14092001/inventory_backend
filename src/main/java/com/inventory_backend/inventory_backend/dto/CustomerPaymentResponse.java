package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPaymentResponse {
    private String status;
    private String message;
    private BigDecimal paidAmount;
    private BigDecimal balanceAfter;
    private Long ledgerId;
}
