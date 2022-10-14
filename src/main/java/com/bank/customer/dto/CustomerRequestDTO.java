package com.bank.customer.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO {
    private String cin;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
}
