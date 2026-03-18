package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.ProductStockResponse;
import com.inventory_backend.inventory_backend.entity.Product;
import com.inventory_backend.inventory_backend.entity.StockLedger;
import com.inventory_backend.inventory_backend.repository.ProductRepository;
import com.inventory_backend.inventory_backend.repository.StockLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockLedgerRepository repo;

    private final ProductRepository productRepository;

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

        if (lastClosing < qty) {
            throw new RuntimeException(
                    "Insufficient stock! Available: " + lastClosing + ", required: " + qty
            );
        }

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
    public ProductStockResponse getCurrentStock(Long productId) {

        Product product=productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product Not Found"));

        Double closing= repo.getLastClosingStock(productId);

        return new ProductStockResponse(
                product.getProductId(),
                product.getName(),
                product.getUnit(),
                product.getDescription(),
                product.getPrefixPrice(),
                closing
        );

    }
}