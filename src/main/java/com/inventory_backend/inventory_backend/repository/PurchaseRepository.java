package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Purchase;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {



    @Query("SELECT AVG(pd.price) FROM PurchaseDetails pd WHERE pd.product.productId = :productId AND pd.purchase.supplier.supplierId = :supplierId")
    Double getAveragePurchasePriceForSupplierProduct(Long productId, Long supplierId);




}
