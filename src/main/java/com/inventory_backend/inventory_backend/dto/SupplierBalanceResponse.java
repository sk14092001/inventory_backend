package com.inventory_backend.inventory_backend.dto;

import com.inventory_backend.inventory_backend.entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierBalanceResponse {
    private Long supplierId;
    private String name;
    private String phone;
    private String email;
    private String address;

    private BigDecimal balance;
}
