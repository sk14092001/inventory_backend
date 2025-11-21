package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CustomerResponse {
    private Long customerId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createdAt;

    public CustomerResponse(Long customerId, String name, String phone, String email, String address, LocalDateTime createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdAt = createdAt;
    }

    public CustomerResponse(Long customerId, String name, String phone, String email, LocalDateTime createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }


}
