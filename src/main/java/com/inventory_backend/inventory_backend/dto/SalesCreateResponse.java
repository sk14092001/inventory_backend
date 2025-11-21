package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesCreateResponse {
//    private String status;
//    private String message;
//    private List<String> outOfStockItems;
//    private Sales data;
//    private List<SalesItemResponse> items;

    private String status;
    private String message;
    private Long saleId;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal grandTotal;
    private List<SalesItemResponse> items;
    private List<String> outOfStockItems;


    public SalesCreateResponse(String status, String message, Long saleId, BigDecimal totalAmount,
                               BigDecimal discount, BigDecimal grandTotal, List<SalesItemResponse> items) {
        this.status = status;
        this.message = message;
        this.saleId = saleId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.grandTotal = grandTotal;
        this.items = items;
        this.outOfStockItems = new ArrayList<>();
    }
}
