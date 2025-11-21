package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.ProfitLossResponse;
import com.inventory_backend.inventory_backend.entity.ProfitAndLoss;
import com.inventory_backend.inventory_backend.entity.SalesItem;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.repository.ProfitAndLossRepository;
import com.inventory_backend.inventory_backend.repository.PurchaseDetailsRepository;
import com.inventory_backend.inventory_backend.repository.SalesItemRepository;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfitLossService {

    @Autowired
    private SalesItemRepository salesItemRepo;
    @Autowired
    private PurchaseDetailsRepository purchaseRepo;
    @Autowired
    private ProfitAndLossRepository plRepo;

    @Autowired
    private SupplierRepository supplierRepository;

    public ProfitLossResponse calculateSupplierProfitLoss(
            Long supplierId,
            LocalDate start,
            LocalDate end,
            String periodType) {

        // 1. Find products supplied by supplier
        List<Long> productIds = purchaseRepo.findProductIdsBySupplier(supplierId);
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier Not Found"));

        String supplierName = supplier.getName();


        if (productIds.isEmpty()) {
            return new ProfitLossResponse(0.0, 0.0, 0.0, periodType, start, end);
        }

        // 2. Get sale items for these products
        List<SalesItem> saleItems = salesItemRepo.findByProductIdsAndInvoiceDate(productIds, start, end);

        double totalSales = 0.0;
        double totalPurchase = 0.0;

        for (SalesItem si : saleItems) {

            Long productId = si.getProduct().getProductId();

            Double avgPurchase = purchaseRepo.getAveragePurchasePriceForSupplierProduct(productId, supplierId);
            if (avgPurchase == null) avgPurchase = 0.0;

            double saleAmount = si.getSellingPrice().doubleValue() * si.getQty().doubleValue();
            double purchaseAmount = avgPurchase * si.getQty().doubleValue();

            totalSales += saleAmount;
            totalPurchase += purchaseAmount;
        }

        double profit = totalSales - totalPurchase;

        // Save
        ProfitAndLoss pl = ProfitAndLoss.builder()
                .periodType(periodType)
                .supplierId(supplierId)
                .periodStart(start)
                .periodEnd(end)
                .totalPurchase(totalPurchase)
                .totalSales(totalSales)
                .totalProfit(profit)
                .build();

        plRepo.save(pl);

        return new ProfitLossResponse(
                supplierId,
                supplierName,
                totalPurchase,
                totalSales,
                profit,
                periodType,
                start,
                end
        );
    }

    public ProfitLossResponse calculateOverallProfitLoss(
            LocalDate start,
            LocalDate end,
            String periodType) {

        // Convert LocalDate to LocalDateTime for the full day
        LocalDateTime startDateTime = start.atStartOfDay();        // 00:00:00
        LocalDateTime endDateTime = end.atTime(23, 59, 59);       // 23:59:59

        // 1. Total purchase amount
        Double totalPurchase = purchaseRepo.getTotalPurchaseAmount(startDateTime.toLocalDate(), endDateTime.toLocalDate());

        // 2. Total sales amount
        Double totalSales = salesItemRepo.getTotalSalesAmountByInvoiceDate(startDateTime.toLocalDate(), endDateTime.toLocalDate());


        if (totalPurchase == null) totalPurchase = 0.0;
        if (totalSales == null) totalSales = 0.0;


        Double profit = totalSales - totalPurchase;


        ProfitAndLoss pl = ProfitAndLoss.builder()
                .periodType(periodType)
                .periodStart(startDateTime.toLocalDate())
                .periodEnd(endDateTime.toLocalDate())
                .totalPurchase(totalPurchase)
                .totalSales(totalSales)
                .totalProfit(profit)
                .build();

        plRepo.save(pl);


        return new ProfitLossResponse(
                null,
                "OVERALL",
                totalPurchase,
                totalSales,
                profit,
                periodType,
                startDateTime.toLocalDate(),
                endDateTime.toLocalDate()
        );
    }

}

