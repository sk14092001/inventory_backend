package com.inventory_backend.inventory_backend.dto;

import lombok.Data;

@Data
public class CustomerRequest {

    private String name;
    private String phone;
    private String email;
    private String address;
}
