package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.entity.StockLedger;
import com.inventory_backend.inventory_backend.repository.StockLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockLedgerRepository repo;

    public void stockIn(Product product, Long purchaseDetailId, double qty) {

        Double lastClosing = repo.getLastClosingStock(product.getProductId());
        if (lastClosing == null) lastClosing = 0.0;

        double closing = lastClosing + qty;

        StockLedger ledger = StockLedger.builder()
                .product(product)
                .transactionType("PURCHASE")
                .transactionId(purchaseDetailId)
                .qtyIn(qty)
                .qtyOut(0.0)
                .closingStock(closing)
                .transactionDate(LocalDate.now())
                .remarks("Purchase stock added")
                .build();

        repo.save(ledger);
    }

    public void stockOut(Product product, Long salesDetailId, double qty) {

        Double lastClosing = repo.getLastClosingStock(product.getProductId());
        if (lastClosing == null) lastClosing = 0.0;

        double closing = lastClosing - qty;

        StockLedger ledger = StockLedger.builder()
                .product(product)
                .transactionType("SALE")
                .transactionId(salesDetailId)
                .qtyIn(0.0)
                .qtyOut(qty)
                .closingStock(closing)
                .transactionDate(LocalDate.now())
                .remarks("Sold stock")
                .build();

        repo.save(ledger);
    }
}
