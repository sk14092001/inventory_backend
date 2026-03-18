package com.inventory_backend.inventory_backend.dto;

import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierAdvanceLedger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierLedgerResponse {

    private SupplierResponseDTO supplier;
    private BigDecimal balance;
    private List<SupplierLedgerDTO> ledger;

}
