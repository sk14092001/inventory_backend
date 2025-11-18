package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.ProfitAndLoss;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProfitAndLossRepository extends JpaRepository<ProfitAndLoss, Long> {}
