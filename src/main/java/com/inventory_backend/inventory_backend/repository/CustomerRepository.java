package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CustomerRepository extends JpaRepository<Customer, Long> {}