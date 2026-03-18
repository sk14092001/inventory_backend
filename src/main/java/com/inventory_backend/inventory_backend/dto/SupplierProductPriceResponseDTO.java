package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierProductPriceResponseDTO{
    private Long priceId;
    private Long supplierId;
    private Long productId;
    private Double price;
    private LocalDate validFrom;
    private LocalDate validTo;
}
