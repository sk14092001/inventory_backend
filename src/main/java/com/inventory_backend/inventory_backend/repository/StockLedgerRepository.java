package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.StockLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockLedgerRepository extends JpaRepository<StockLedger, Long> {

    @Query("""
        SELECT s.closingStock 
        FROM StockLedger s 
        WHERE s.product.productId = :productId 
        ORDER BY s.stockLedgerId DESC
        LIMIT 1
    """)
    Double getLastClosingStock(@Param("productId") Long productId);
}
