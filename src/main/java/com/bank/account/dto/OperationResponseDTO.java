package com.bank.account.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationResponseDTO {
    private Long id;
    private Long accountId;
    private String operationType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime operationDate;
}
