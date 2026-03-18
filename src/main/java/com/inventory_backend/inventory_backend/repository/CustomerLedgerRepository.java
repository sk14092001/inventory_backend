package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.CustomerLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerLedgerRepository extends JpaRepository<CustomerLedger,Long> {
    @Query("SELECT l.balanceAfterTransaction FROM CustomerLedger l " +
            "WHERE l.customer.customerId = :customerId " +
            "ORDER BY l.ledgerId DESC LIMIT 1")
    BigDecimal getLastBalance(Long customerId);
    List<CustomerLedger> findByCustomer_CustomerIdOrderByTransactionDateAsc(Long customerId);
}
