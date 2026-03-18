package com.inventory_backend.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLedgerResponse {

    private CustomerResponse customer;
    private BigDecimal currentBalance;
    private List<CustomerLedgerDTO> ledgerList;
}
