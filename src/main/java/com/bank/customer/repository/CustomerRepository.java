package com.bank.customer.repository;

import com.bank.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCin(String cin);
    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
}
