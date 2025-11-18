package com.inventory_backend.inventory_backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SaleRequest {
    private Long customerId;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private List<SaleItemDto> items;
}