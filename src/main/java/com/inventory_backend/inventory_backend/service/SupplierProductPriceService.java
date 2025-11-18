package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.SupplierProductPriceDTO;
import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierProductPrice;
import com.inventory_backend.inventory_backend.repository.ProductRepository;
import com.inventory_backend.inventory_backend.repository.SupplierProductPriceRepository;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SupplierProductPriceService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final SupplierProductPriceRepository sppRepository;

    public SupplierProductPrice saveMapping(SupplierProductPriceDTO dto) {

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        SupplierProductPrice spp = SupplierProductPrice.builder()
                .supplier(supplier)
                .product(product)
                .price(product.getPrefixPrice())
                .validFrom(LocalDate.now())
                .validTo(null)
                .build();

        return sppRepository.save(spp);
    }
}

