package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {}