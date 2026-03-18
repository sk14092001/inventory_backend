package com.inventory_backend.inventory_backend.dto;

import lombok.Data;

import java.util.List;
@Data
public class LoginResponse {

    private String token;
    private String email;
    private List<String> roles;
}
