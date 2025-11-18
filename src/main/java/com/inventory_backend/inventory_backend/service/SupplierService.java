package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }
}
