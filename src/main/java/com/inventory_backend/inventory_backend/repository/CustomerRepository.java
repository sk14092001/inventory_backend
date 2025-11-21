package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT COUNT(s) FROM Sales s WHERE s.customer.customerId = :customerId")
    long countSalesForCustomer(Long customerId);

    List<Customer> findByActiveTrue();
}