package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierProductPrice;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
public interface SupplierProductPriceRepository extends JpaRepository<SupplierProductPrice, Long> {
    Optional<SupplierProductPrice> findTopBySupplierSupplierIdAndProductProductIdOrderByValidFromDesc(Long supplierId, Long productId);

    @Query("SELECT s FROM SupplierProductPrice s " +
            "WHERE s.supplier.supplierId = :supplierId " +
            "AND s.product.productId = :productId " +
            "AND (s.validTo IS NULL OR s.validTo >= CURRENT_DATE)")
    Optional<SupplierProductPrice>  findBySupplierAndProduct(Long supplierId, Long productId);
}