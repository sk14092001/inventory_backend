package com.inventory_backend.inventory_backend.dto;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierProductPriceDTO {
    private Long supplierId;
    private Long productId;
    private Double price;
}
