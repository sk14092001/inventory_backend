package com.inventory_backend.inventory_backend.dto;

import lombok.Data;

import java.util.Set;
@Data
public class SignUpRequest {

    private String username;
    private String email;
    private String password;

    private Set<String> roles; // ["DEVELOPER", "TL"]
}