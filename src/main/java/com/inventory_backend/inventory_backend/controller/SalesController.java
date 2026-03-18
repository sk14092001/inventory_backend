package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.*;
import com.inventory_backend.inventory_backend.entity.Customer;
import com.inventory_backend.inventory_backend.service.CustomerService;
import com.inventory_backend.inventory_backend.service.SalesService;
import com.itextpdf.text.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;
    private final CustomerService customerService;


    @PostMapping("/calculate-total")
    public SalesTotalResponse calculateTotal(@RequestBody SalesRequest request) {
        return salesService.calculateTotal(request);
    }


    @PostMapping("/create")
    public SalesCreateResponse createSale(
            @RequestParam Long customerId,
            @RequestParam(required = false) BigDecimal paidAmount,
            @RequestBody SalesRequest request
    ) {
        Customer customer = customerService.getCustomerById(customerId);
        return salesService.createSale(request, customer, paidAmount);
    }


    @PostMapping("/pay-only")
    public CustomerPaymentResponse payOnly(
            @RequestParam Long customerId,
            @RequestParam BigDecimal amount
    ) {
        Customer customer = customerService.getCustomerById(customerId);
        return salesService.payOnly(customer, amount);
    }


    @GetMapping("/customer/{customerId}/balance")
    public CustomerBalanceResponse getCustomerBalance(@PathVariable Long customerId) {
        return salesService.getCustomerBalanceDetails(customerId);
    }


    @GetMapping("/customer/{customerId}/ledger")
    public CustomerLedgerResponse getCustomerLedger(@PathVariable Long customerId) {
        return salesService.getCustomerLedger(customerId);
    }

    @GetMapping("/customerLedger/{customerId}")
    public ResponseEntity<byte[]> getCustomerLedgerPdf(
            @PathVariable Long customerId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
            )
    {
        byte[] pdf=salesService.getCustomerLedgerPdf(customerId,start,end);

        return ResponseEntity.ok().
                header("Content-Type","application/pdf")
                .header("Content-Disposition","attachment; filename=customer-ledger.pdf").
                body(pdf);

    }
}
