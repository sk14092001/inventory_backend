//package com.inventory_backend.inventory_backend.repository;
//import com.inventory_backend.inventory_backend.entity.StockLedger;
//import org.springframework.data.jpa.repository.*;
//import org.springframework.data.repository.query.Param;
//
//public interface StockLedgerRepository extends JpaRepository<StockLedger, Long> {
//    @Query("SELECT COALESCE(SUM(s.qty),0) FROM StockLedger s WHERE s.product.productId = :productId")
//    Double sumQtyByProduct(@Param("productId") Long productId);
//}
