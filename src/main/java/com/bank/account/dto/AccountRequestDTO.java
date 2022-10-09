package com.bank.account.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDTO {
    private Long customerId;
    private String accountNumber;
    private String currency;
    private BigDecimal initialBalance;
}