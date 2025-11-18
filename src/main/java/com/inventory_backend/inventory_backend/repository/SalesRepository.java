package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SalesRepository extends JpaRepository<Sales, Long> {}
