package com.inventory_backend.inventory_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SalesRequest {
    private Long customerId;
    private String invoiceNo;
    private List<SalesItemRequest> items;
    private BigDecimal discount;
}