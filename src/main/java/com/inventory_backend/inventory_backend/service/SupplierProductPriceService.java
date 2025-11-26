package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.SupplierProductPriceDTO;
import com.inventory_backend.inventory_backend.dto.SupplierProductPriceResponseDTO;
import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierProductPrice;
import com.inventory_backend.inventory_backend.repository.ProductRepository;
import com.inventory_backend.inventory_backend.repository.SupplierProductPriceRepository;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierProductPriceService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final SupplierProductPriceRepository sppRepository;

    public SupplierProductPriceResponseDTO saveMapping(SupplierProductPriceDTO dto) {

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

        SupplierProductPrice saved = sppRepository.save(spp);


        SupplierProductPriceResponseDTO response = new SupplierProductPriceResponseDTO();
        response.setPriceId(saved.getPriceId());
        response.setSupplierId(saved.getSupplier().getSupplierId());
        response.setProductId(saved.getProduct().getProductId());
        response.setPrice(saved.getPrice());
        response.setValidFrom(saved.getValidFrom());
        response.setValidTo(saved.getValidTo());

        return response;
    }

    public List<SupplierProductPriceResponseDTO> saveMultipleMappings(SupplierProductPriceDTO[] dtos) {

        List<SupplierProductPriceResponseDTO> responseList = new ArrayList<>();

        for (SupplierProductPriceDTO dto : dtos) {
            responseList.add(saveMapping(dto));
        }

        return responseList;
    }
}
