package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/save")
    public ResponseEntity<Supplier> saveSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.saveSupplier(supplier));
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAll() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplier(id));
    }
}
