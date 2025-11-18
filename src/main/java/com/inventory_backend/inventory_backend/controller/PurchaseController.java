package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.PurchaseRequest;
import com.inventory_backend.inventory_backend.dto.PurchaseTotalResponse;
import com.inventory_backend.inventory_backend.entity.Purchase;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.entity.SupplierAdvanceLedger;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import com.inventory_backend.inventory_backend.repository.SupplierAdvanceLedgerRepository;
import com.inventory_backend.inventory_backend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final SupplierRepository supplierRepository;
    private final SupplierAdvanceLedgerRepository ledgerRepository;

    // ------------------------------------------------------------
    // 1️⃣  Calculate Total Before Creating Purchase
    // ------------------------------------------------------------
    @PostMapping("/calculate/{supplierId}")
    public PurchaseTotalResponse calculateTotal(
            @PathVariable Long supplierId,
            @RequestBody PurchaseRequest request) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return purchaseService.calculateTotal(request, supplier);
    }

    // ------------------------------------------------------------
    // 2️⃣  Create Purchase (with or without paidAmount)
    // ------------------------------------------------------------
    @PostMapping("/create/{supplierId}")
    public Purchase createPurchase(
            @PathVariable Long supplierId,
            @RequestParam(required = false) BigDecimal paidAmount,
            @RequestBody PurchaseRequest request) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return purchaseService.createPurchase(request, supplier, paidAmount);
    }

    // ------------------------------------------------------------
    // 3️⃣  Pay Amount WITHOUT Purchase (Pure Advance)
    // ------------------------------------------------------------
    @PostMapping("/pay-only/{supplierId}")
    public SupplierAdvanceLedger payOnly(
            @PathVariable Long supplierId,
            @RequestParam BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be positive");

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return purchaseService.addStandaloneAdvancePayment(supplier, amount);
    }

    // ------------------------------------------------------------
    // 4️⃣  Get Supplier Ledger History
    // ------------------------------------------------------------
//    @GetMapping("/ledger/{supplierId}")
//    public List<SupplierAdvanceLedger> getLedger(@PathVariable Long supplierId) {
//        return ledgerRepository.findBySupplier_SupplierIdOrderByTransactionDateAsc(supplierId);
//    }

    // ------------------------------------------------------------
    // 5️⃣  Get Latest Balance (BalanceAfterTransaction)
    // ------------------------------------------------------------
    @GetMapping("/balance/{supplierId}")
    public BigDecimal getSupplierBalance(@PathVariable Long supplierId) {
        BigDecimal bal = ledgerRepository.getLastBalance(supplierId);
        return bal != null ? bal : BigDecimal.ZERO;
    }


}
