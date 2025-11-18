package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }
}

