package com.bank.customer.controller;

import com.bank.customer.dto.CustomerRequestDTO;
import com.bank.customer.dto.CustomerResponseDTO;
import com.bank.customer.service.CustomerService;
import com.bank.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO customer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(customer, "Customer created successfully"));
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomer(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @GetMapping("/find/{cin}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerByCin(@PathVariable String cin) {
        CustomerResponseDTO customer = customerService.getCustomerByCin(cin);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @GetMapping("/list")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> searchCustomers(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        List<CustomerResponseDTO> customers;
        if (firstName != null) {
            customers = customerService.searchByFirstName(firstName);
        } else if (lastName != null) {
            customers = customerService.searchByLastName(lastName);
        } else {
            customers = customerService.getAllCustomers();
        }
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO customer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success(customer, "Customer updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer deleted successfully"));
    }
}
