package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class SaleDetailsResponse {
    private Long salesId;

    private String invoiceNo;

    private LocalDate invoiceDate;

    private BigDecimal totalAmount;

    private BigDecimal discount;

    private BigDecimal grandTotal;

    private String customerName;

    private List<SalesItemResponse> items;
}
