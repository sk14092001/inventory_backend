package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SupplierProductPriceRepository extends JpaRepository<SupplierProductPrice, Long> {
    Optional<SupplierProductPrice> findTopBySupplierSupplierIdAndProductProductIdOrderByValidFromDesc(Long supplierId, Long productId);
    Optional<SupplierProductPrice> findBySupplierAndProduct(
            Supplier supplier, Product product);
}