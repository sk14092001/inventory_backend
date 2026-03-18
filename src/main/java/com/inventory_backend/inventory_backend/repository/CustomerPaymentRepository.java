package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Customer;
import com.inventory_backend.inventory_backend.entity.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;

@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment,Long> {


    Page<CustomerPayment> findByCustomerAndAmountRemainingGreaterThanOrderByPaymentDateAsc(Customer customer, BigDecimal zero, org.springframework.data.domain.Pageable pageable);
}
