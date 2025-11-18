package com.inventory_backend.inventory_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierProductPriceDTO {
    private Long supplierId;
    private Long productId;
    private Double price;
}
