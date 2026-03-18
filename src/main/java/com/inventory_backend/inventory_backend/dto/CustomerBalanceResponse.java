package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBalanceResponse {
    private Long customerId;
    private String name;
    private String phone;
    private String email;
    private BigDecimal balance;
}
