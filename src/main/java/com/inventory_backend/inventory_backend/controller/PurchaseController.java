package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.*;
import com.inventory_backend.inventory_backend.entity.Supplier;
import com.inventory_backend.inventory_backend.repository.SupplierRepository;
import com.inventory_backend.inventory_backend.repository.SupplierAdvanceLedgerRepository;
import com.inventory_backend.inventory_backend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final SupplierRepository supplierRepository;
    private final SupplierAdvanceLedgerRepository ledgerRepository;

    @PostMapping("/calculate/{supplierId}")
    public PurchaseTotalResponse calculateTotal(
            @PathVariable Long supplierId,
            @RequestBody PurchaseRequest request) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return purchaseService.calculateTotal(request, supplier);
    }

    @PostMapping("/create/{supplierId}")
    public PurchaseCreateResponse createPurchase(
            @PathVariable Long supplierId,
            @RequestParam(required = false) BigDecimal paidAmount,
            @RequestBody PurchaseRequest request) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return purchaseService.createPurchase(request, supplier, paidAmount);
    }

    @PostMapping("/pay-only/{supplierId}")
    public SupplierPaymentResponse payOnly(
            @PathVariable Long supplierId,
            @RequestParam BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be positive");

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return purchaseService.payOnly(supplier, amount);
    }


    @GetMapping("/ledger/{supplierId}")
    public SupplierLedgerResponse getLedger(@PathVariable Long supplierId
    , @RequestParam(required = false) LocalDate start,@RequestParam (required = false)LocalDate end) {
        return purchaseService.getSupplierLedger(supplierId,start,end);
    }


    @GetMapping("/balance/{supplierId}")
    public SupplierBalanceResponse getSupplierBalance(@PathVariable Long supplierId) {

        return purchaseService.getSupplierBalanceDetails(supplierId);
    }
    @GetMapping("/supplier-ledger/pdf/{supplierId}")
    public ResponseEntity<byte[]> downloadLedgerPdf(
            @PathVariable Long supplierId,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
    )
    {
        byte[] pdf= purchaseService.getSupplierLedgerPdf(supplierId,start,end);

        return ResponseEntity.ok().header("Content-Type","application/pdf")
                .header("Content-Disposition","attachment;supplier-ledger.pdf").body(pdf);

    }


}
