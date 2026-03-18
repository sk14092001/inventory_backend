package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockResponse {

    private Long productId;

    private String name;

    private String unit;

    private String description;

    private Double prefixPrice;

    private Double currentStock;


}
