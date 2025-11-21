package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.ProductRequest;
import com.inventory_backend.inventory_backend.dto.ProductResponse;
import com.inventory_backend.inventory_backend.dto.ProductStockResponse;
import com.inventory_backend.inventory_backend.service.ProductService;
import com.inventory_backend.inventory_backend.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService service;

    @PostMapping("/save")
    public ResponseEntity<ProductResponse> saveProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.saveProduct(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("getStock/{id}")
    public ResponseEntity<ProductStockResponse> getStock(@PathVariable("id") Long productId)
    {
        return ResponseEntity.ok(service.getCurrentStock(productId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(Long id,@RequestBody ProductRequest request)
    {
        return ResponseEntity.ok(productService.updateProduct(id,request));
    }

    @DeleteMapping("delete/{id}")
    public  ResponseEntity<String> delete(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(productService.delete(id));
    }

}

