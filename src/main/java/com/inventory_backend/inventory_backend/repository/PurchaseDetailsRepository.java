package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.PurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetails, Long> {
    // more queries can be added for reports
    List<PurchaseDetails> findByPurchaseInvoiceDate(LocalDate date);
}