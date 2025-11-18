package com.inventory_backend.inventory_backend.dto;


import lombok.Data;

@Data
public class SaleItemDto {
    private Long productId;
    private Double qty;
    private Double sellingPrice;
}
