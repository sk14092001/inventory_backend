package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.CustomerRequest;
import com.inventory_backend.inventory_backend.dto.CustomerResponse;
import com.inventory_backend.inventory_backend.entity.Customer;
import com.inventory_backend.inventory_backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private  CustomerService customerService;

    @PostMapping("/save")
    public CustomerResponse create(@RequestBody CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @PutMapping("/update/{id}")
    public CustomerResponse update(@PathVariable Long id,
                                   @RequestBody CustomerRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @GetMapping("/getOne/{id}")
    public CustomerResponse getOne(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping("/getAll")
    public List<CustomerResponse> getAll() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }



}
