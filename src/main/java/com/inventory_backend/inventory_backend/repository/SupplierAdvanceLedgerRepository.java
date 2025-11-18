package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.SupplierAdvanceLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface SupplierAdvanceLedgerRepository extends JpaRepository<SupplierAdvanceLedger, Long> {

    @Query(
            value = "SELECT balance_after_transaction FROM supplier_advance_ledger " +
                    "WHERE supplier_id = :supplierId " +
                    "ORDER BY transaction_date DESC, ledger_id DESC " +
                    "LIMIT 1",
            nativeQuery = true
    )
    BigDecimal getLastBalance(@Param("supplierId") Long supplierId);

}
