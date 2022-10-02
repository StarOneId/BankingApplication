package com.bank.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String refreshToken;
    private Long expiresIn;
    private String username;
}
