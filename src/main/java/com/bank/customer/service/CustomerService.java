package com.bank.customer.service;

import com.bank.customer.dto.CustomerRequestDTO;
import com.bank.customer.dto.CustomerResponseDTO;
import com.bank.customer.entity.Customer;
import com.bank.customer.repository.CustomerRepository;
import com.bank.common.exception.BusinessException;
import com.bank.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        if (customerRepository.findByCin(request.getCin()).isPresent()) {
            throw new BusinessException("CUSTOMER_EXISTS", "Customer with CIN " + request.getCin() + " already exists");
        }

        Customer customer = Customer.builder()
            .cin(request.getCin())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .address(request.getAddress())
            .city(request.getCity())
            .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToDTO(savedCustomer);
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToDTO(customer);
    }

    public CustomerResponseDTO getCustomerByCin(String cin) {
        Customer customer = customerRepository.findByCin(cin)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with CIN: " + cin));
        return mapToDTO(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<CustomerResponseDTO> searchByFirstName(String firstName) {
        return customerRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<CustomerResponseDTO> searchByLastName(String lastName) {
        return customerRepository.findByLastNameContainingIgnoreCase(lastName).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToDTO(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerResponseDTO mapToDTO(Customer customer) {
        return CustomerResponseDTO.builder()
            .id(customer.getId())
            .cin(customer.getCin())
            .firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .email(customer.getEmail())
            .phone(customer.getPhone())
            .address(customer.getAddress())
            .city(customer.getCity())
            .createdAt(customer.getCreatedAt())
            .updatedAt(customer.getUpdatedAt())
            .build();
    }
}
