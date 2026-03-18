package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierAdvance;
import com.inventory_backend.inventory_backend.entity.SupplierAdvanceLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SupplierAdvanceRepository extends JpaRepository<SupplierAdvance, Long> {

    Page<SupplierAdvance> findBySupplierAndAmountRemainingGreaterThanOrderByAdvanceDateAsc(
            Supplier supplier, BigDecimal amount, Pageable pageable);



}