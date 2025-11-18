package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {}
