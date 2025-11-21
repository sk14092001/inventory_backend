package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.SupplierProductPriceDTO;
import com.inventory_backend.inventory_backend.dto.SupplierProductPriceResponseDTO;
import com.inventory_backend.inventory_backend.entity.SupplierProductPrice;
import com.inventory_backend.inventory_backend.service.SupplierProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/supplier-product-price")
@RequiredArgsConstructor
public class SupplierProductPriceController {

    private final SupplierProductPriceService sppService;

    @PostMapping("/map")
    public ResponseEntity<SupplierProductPriceResponseDTO> mapSupplierToProduct(
            @RequestBody SupplierProductPriceDTO dto) {

        SupplierProductPriceResponseDTO response =
                sppService.saveMapping(dto);

        return ResponseEntity.ok(response);
    }
}
