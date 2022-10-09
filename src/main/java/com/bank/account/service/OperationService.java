package com.bank.account.service;

import com.bank.account.dto.OperationResponseDTO;
import com.bank.account.entity.Operation;
import com.bank.account.repository.OperationRepository;
import com.bank.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    public List<OperationResponseDTO> getOperationsByAccountId(Long accountId) {
        return operationRepository.findByAccountIdOrderByOperationDateDesc(accountId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public OperationResponseDTO getOperationById(Long id) {
        Operation operation = operationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Operation not found with id: " + id));
        return mapToDTO(operation);
    }

    private OperationResponseDTO mapToDTO(Operation operation) {
        return OperationResponseDTO.builder()
            .id(operation.getId())
            .accountId(operation.getAccountId())
            .operationType(operation.getOperationType())
            .amount(operation.getAmount())
            .description(operation.getDescription())
            .operationDate(operation.getOperationDate())
            .build();
    }
}
