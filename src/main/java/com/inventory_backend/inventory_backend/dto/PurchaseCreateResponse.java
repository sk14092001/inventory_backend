package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseCreateResponse {

    private String status;                 // SUCCESS / FAIL
    private String message;

    private Long purchaseId;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal grandTotal;

    private List<PurchaseDetailsResponse> items;


}
