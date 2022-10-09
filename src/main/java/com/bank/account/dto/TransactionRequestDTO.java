package com.bank.account.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDTO {
    private Long accountId;
    private BigDecimal amount;
    private String description;
}
