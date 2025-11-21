package com.inventory_backend.inventory_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLedgerDTO {
    private Long ledgerId;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
    private LocalDate transactionDate;
    private String referenceType;
    private Long referenceId;
    private String remarks;

}
