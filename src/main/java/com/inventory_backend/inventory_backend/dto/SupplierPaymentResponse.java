package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SupplierPaymentResponse {

    private String status;           // SUCCESS
    private String message;

    private BigDecimal amountPaid;
    private BigDecimal updatedBalance;

    private Long ledgerEntryId;
}
