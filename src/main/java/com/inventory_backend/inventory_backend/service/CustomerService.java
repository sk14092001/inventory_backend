package com.inventory_backend.inventory_backend.service;

import com.inventory_backend.inventory_backend.dto.CustomerRequest;
import com.inventory_backend.inventory_backend.dto.CustomerResponse;
import com.inventory_backend.inventory_backend.entity.Customer;
import com.inventory_backend.inventory_backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    public CustomerResponse createCustomer(CustomerRequest req) {

        Customer customer = Customer.builder()
                .name(req.getName())
                .phone(req.getPhone())
                .email(req.getEmail())
                .address(req.getAddress())
                .createdAt(LocalDateTime.now())
                .build();

        customer = customerRepo.save(customer);

        return buildResponse(customer);
    }


    public CustomerResponse updateCustomer(Long id, CustomerRequest req) {

        Customer existing = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        existing.setName(req.getName());
        existing.setPhone(req.getPhone());
        existing.setEmail(req.getEmail());
        existing.setAddress(req.getAddress());

        existing = customerRepo.save(existing);

        return buildResponse(existing);
    }

    public CustomerResponse getCustomer(Long id) {
        Customer c = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return buildResponse(c);
    }


    public List<CustomerResponse> getAllCustomers() {

        List<Customer> customers = customerRepo.findAll();
        List<CustomerResponse> list = new ArrayList<>();

        for (Customer c : customers) {
            list.add(buildResponse(c));
        }

        return list;
    }

    public String deleteCustomer(Long id) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        long salesCount = customerRepo.countSalesForCustomer(id);

        if (salesCount > 0) {
            throw new RuntimeException("Cannot delete. Customer has sales records.");
        }


        customer.setActive(false);
        customerRepo.save(customer);

        return "Customer deleted successfully (soft delete)";
    }



    private CustomerResponse buildResponse(Customer c) {

        CustomerResponse res = new CustomerResponse();
        res.setCustomerId(c.getCustomerId());
        res.setName(c.getName());
        res.setPhone(c.getPhone());
        res.setEmail(c.getEmail());
        res.setAddress(c.getAddress());
        res.setCreatedAt(c.getCreatedAt());

        return res;
    }
    public Customer getCustomerById(Long customerId) {
        return customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }
}
