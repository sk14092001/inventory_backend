package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SupplierRepository extends JpaRepository<Supplier, Long> {}
