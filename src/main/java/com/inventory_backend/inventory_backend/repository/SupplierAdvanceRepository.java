package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierAdvance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface SupplierAdvanceRepository extends JpaRepository<SupplierAdvance, Long> {

    // Find only advances that still have remaining amount (usable), oldest first (FIFO)
    Page<SupplierAdvance> findBySupplierAndAmountRemainingGreaterThanOrderByAdvanceDateAsc(
            Supplier supplier, BigDecimal amount, Pageable pageable);
}