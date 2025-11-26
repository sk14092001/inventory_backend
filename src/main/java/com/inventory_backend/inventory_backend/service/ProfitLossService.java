package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.ProfitLossResponse;
import com.inventory_backend.inventory_backend.dto.ProfitLossResponseDto;
import com.inventory_backend.inventory_backend.entity.ProfitAndLoss;
import com.inventory_backend.inventory_backend.entity.SalesItem;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.repository.ProfitAndLossRepository;
import com.inventory_backend.inventory_backend.repository.PurchaseDetailsRepository;
import com.inventory_backend.inventory_backend.repository.SalesItemRepository;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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

        List<Long> productIds = purchaseRepo.findProductIdsBySupplier(supplierId);

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier Not Found"));

        String supplierName = supplier.getName();

        if (productIds.isEmpty()) {
            return new ProfitLossResponse(
                    supplierId,
                    supplierName,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    periodType,
                    start,
                    end
            );
        }

        List<SalesItem> saleItems = salesItemRepo.findByProductIdsAndInvoiceDate(productIds, start, end);

        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalPurchase = BigDecimal.ZERO;

        for (SalesItem si : saleItems) {

            Long productId = si.getProduct().getProductId();

            Double avgPurchasePrice = purchaseRepo.getAveragePurchasePriceForSupplierProduct(productId, supplierId);
            BigDecimal avgPurchase = avgPurchasePrice != null ?
                    BigDecimal.valueOf(avgPurchasePrice) : BigDecimal.ZERO;

            BigDecimal saleAmount = si.getSellingPrice().multiply(si.getQty());
            BigDecimal purchaseAmount = avgPurchase.multiply(si.getQty());

            totalSales = totalSales.add(saleAmount);
            totalPurchase = totalPurchase.add(purchaseAmount);
        }

        BigDecimal profit = totalSales.subtract(totalPurchase);


        ProfitAndLoss pl = ProfitAndLoss.builder()
                .periodType(periodType)
                .supplierId(supplierId)
                .periodStart(start)
                .periodEnd(end)
                .totalPurchase(totalPurchase.doubleValue())
                .totalSales(totalSales.doubleValue())
                .totalProfit(profit.doubleValue())
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

        BigDecimal totalPurchase = purchaseRepo.getTotalPurchaseAmount(start, end);
        BigDecimal totalSales = salesItemRepo.getTotalSalesAmountByInvoiceDate(start, end);

        if (totalPurchase == null) totalPurchase = BigDecimal.ZERO;
        if (totalSales == null) totalSales = BigDecimal.ZERO;

        BigDecimal profit = totalSales.subtract(totalPurchase);

        ProfitAndLoss pl = ProfitAndLoss.builder()
                .periodType(periodType)
                .periodStart(start)
                .periodEnd(end)
                .totalPurchase(totalPurchase.doubleValue())
                .totalSales(totalSales.doubleValue())
                .totalProfit(profit.doubleValue())
                .build();

        plRepo.save(pl);

        return new ProfitLossResponse(
                null,
                "OVERALL",
                totalPurchase,
                totalSales,
                profit,
                periodType,
                start,
                end
        );
    }
    public ProfitLossResponseDto calculateOverall() {

        BigDecimal totalPurchase = purchaseRepo.getTotalPurchaseAllTime();
        BigDecimal totalSales = salesItemRepo.getTotalSalesAllTime();

        if (totalPurchase == null) totalPurchase = BigDecimal.ZERO;
        if (totalSales == null) totalSales = BigDecimal.ZERO;

        BigDecimal profit = totalSales.subtract(totalPurchase);

        return new ProfitLossResponseDto(
                null,
                "OVERALL",
                totalPurchase,
                totalSales,
                profit,
                "ALL_TIME"
        );
    }


    public ProfitLossResponseDto calculateSupplier(Long supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier Not Found"));

        BigDecimal totalSales = salesItemRepo.getSupplierSalesAllTime(supplierId);
        BigDecimal totalPurchase = purchaseRepo.getSupplierPurchaseAllTime(supplierId);

        if (totalPurchase == null) totalPurchase = BigDecimal.ZERO;
        if (totalSales == null) totalSales = BigDecimal.ZERO;

        BigDecimal profit = totalSales.subtract(totalPurchase);

        return new ProfitLossResponseDto(
                supplierId,
                supplier.getName(),
                totalPurchase,
                totalSales,
                profit,
                "ALL_TIME"
        );
    }
}
