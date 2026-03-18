package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponseDTO {
    private Long supplierId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}
