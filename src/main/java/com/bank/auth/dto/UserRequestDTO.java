package com.bank.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
}
