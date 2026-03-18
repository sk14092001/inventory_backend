package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.SupplierRequestDTO;
import com.inventory_backend.inventory_backend.dto.SupplierResponseDTO;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public Supplier saveSupplier(SupplierRequestDTO dto) {

        Supplier supplier = Supplier.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();

        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }


    public SupplierResponseDTO toResponse(Supplier s) {

        SupplierResponseDTO dto = new SupplierResponseDTO();
        dto.setSupplierId(s.getSupplierId());
        dto.setName(s.getName());
        dto.setEmail(s.getEmail());
        dto.setPhone(s.getPhone());
        dto.setAddress(s.getAddress());
        dto.setCreatedAt(s.getCreatedAt());

        return dto;
    }
}
