package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.SupplierRequestDTO;
import com.inventory_backend.inventory_backend.dto.SupplierResponseDTO;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/save")
    public ResponseEntity<SupplierResponseDTO> saveSupplier(
            @RequestBody SupplierRequestDTO dto) {

        Supplier saved = supplierService.saveSupplier(dto);
        SupplierResponseDTO res = supplierService.toResponse(saved);

        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAll() {

        List<SupplierResponseDTO> list =
                supplierService.getAllSuppliers().stream()
                        .map(supplierService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getOne(@PathVariable Long id) {

        Supplier supplier = supplierService.getSupplier(id);
        return ResponseEntity.ok(supplierService.toResponse(supplier));
    }
}
