package com.inventory_backend.inventory_backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseRequest {

    private String invoiceNo;
    private List<PurchaseItemDto> items;
    // optional: amountPaidCash, amountDeductedFromAdvance — but we'll auto deduct from advances
}