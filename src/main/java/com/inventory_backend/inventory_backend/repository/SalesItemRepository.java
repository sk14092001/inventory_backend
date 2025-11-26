package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Sales;
import com.inventory_backend.inventory_backend.entity.SalesItem;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {

    @Query("SELECT si FROM SalesItem si JOIN si.sales s " +
            "WHERE s.invoiceDate BETWEEN :start AND :end")
    List<SalesItem> findSalesItemsBetweenDates(LocalDate start, LocalDate end);

    List<SalesItem> findBySales(Sales sale);

    @Query("SELECT s FROM SalesItem s WHERE s.product.productId IN :productIds AND s.sales.invoiceDate BETWEEN :start AND :end")
    List<SalesItem> findByProductIdsAndInvoiceDate(
            List<Long> productIds,
            LocalDate start,
            LocalDate end
    );

    @Query("SELECT COALESCE(SUM(s.grandTotal), 0) FROM Sales s WHERE s.invoiceDate BETWEEN :start AND :end")
    BigDecimal getTotalSalesAmountByInvoiceDate(LocalDate start, LocalDate end);

    @Query("""
    SELECT SUM(s.sellingPrice * s.qty) FROM SalesItem s """)
    BigDecimal getTotalSalesAllTime();

    @Query("""
            SELECT SUM(s.sellingPrice * s.qty) FROM SalesItem s WHERE s.product.productId IN (
    SELECT pd.product.productId
    FROM PurchaseDetails pd
    WHERE pd.purchase.supplier.supplierId = :supplierId) """)
    BigDecimal getSupplierSalesAllTime(Long supplierId);
}


