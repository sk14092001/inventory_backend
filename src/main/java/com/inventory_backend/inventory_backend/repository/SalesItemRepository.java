package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {

    List<SalesItem> findBySalesInvoiceDate(LocalDate invoiceDate);
}
