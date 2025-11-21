package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPaymentRequest {
    private Long customerId;
    private BigDecimal amount;
    private String paymentType; // CASH, UPI, CARD
    private String remarks;
}
