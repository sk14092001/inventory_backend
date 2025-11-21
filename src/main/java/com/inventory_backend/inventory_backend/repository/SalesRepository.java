package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Customer;
import com.inventory_backend.inventory_backend.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByCustomer(Customer customer);

    List<Sales> findByInvoiceDateBetween(LocalDate from, LocalDate to);
}
