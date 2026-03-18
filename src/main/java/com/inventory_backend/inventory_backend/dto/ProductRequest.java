package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest {

    private String name;

    private String unit; // KG or PCS

    private String description;

    private Double prefixPrice;
}
