package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Purchase;
import com.inventory_backend.inventory_backend.entity.PurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetails, Long> {
    // more queries can be added for reports
    List<PurchaseDetails> findByPurchaseInvoiceDate(LocalDate date);

    @Query("SELECT AVG(pd.price) FROM PurchaseDetails pd WHERE pd.product.productId = :productId")
    Double getAveragePurchasePrice(Long productId);

    @Query("SELECT DISTINCT pd.product.productId FROM PurchaseDetails pd " +
            "WHERE pd.purchase.supplier.supplierId = :supplierId")
    List<Long> findProductIdsBySupplier(Long supplierId);

    @Query("SELECT AVG(pd.price) FROM PurchaseDetails pd WHERE pd.product.productId = :productId " +
            "AND pd.purchase.supplier.supplierId = :supplierId")
    Double getAveragePurchasePriceForSupplierProduct(Long productId, Long supplierId);


    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Purchase p WHERE p.invoiceDate BETWEEN :start AND :end")
    Double getTotalPurchaseAmount(LocalDate start, LocalDate end);






}
