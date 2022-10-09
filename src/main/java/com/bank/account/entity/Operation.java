package com.bank.account.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "operation_type", nullable = false, length = 20)
    private String operationType;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    private String description;

    @Column(name = "operation_date", nullable = false, updatable = false)
    private LocalDateTime operationDate;

    @PrePersist
    protected void onCreate() {
        operationDate = LocalDateTime.now();
    }
}
