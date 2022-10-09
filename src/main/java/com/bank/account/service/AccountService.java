package com.bank.account.service;

import com.bank.account.dto.*;
import com.bank.account.entity.Account;
import com.bank.account.entity.Operation;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.OperationRepository;
import com.bank.common.constants.AppConstants;
import com.bank.common.exception.BusinessException;
import com.bank.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository operationRepository;

    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        if (accountRepository.findByAccountNumber(request.getAccountNumber()).isPresent()) {
            throw new BusinessException("ACCOUNT_EXISTS", "Account number already exists");
        }

        Account account = Account.builder()
            .customerId(request.getCustomerId())
            .accountNumber(request.getAccountNumber())
            .currency(request.getCurrency() != null ? request.getCurrency() : AppConstants.DEFAULT_CURRENCY)
            .balance(request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO)
            .status(AppConstants.ACCOUNT_STATUS_ACTIVE)
            .build();

        Account savedAccount = accountRepository.save(account);
        return mapToDTO(savedAccount);
    }

    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return mapToDTO(account);
    }

    public List<AccountResponseDTO> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO request) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        account.setStatus(request.getCurrency());

        Account updatedAccount = accountRepository.save(account);
        return mapToDTO(updatedAccount);
    }

    @Transactional
    public AccountResponseDTO creditAccount(Long accountId, TransactionRequestDTO request) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("INVALID_AMOUNT", "Amount must be greater than zero");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        // Record operation
        Operation operation = Operation.builder()
            .accountId(accountId)
            .operationType(AppConstants.OPERATION_TYPE_CREDIT)
            .amount(request.getAmount())
            .description(request.getDescription())
            .build();
        operationRepository.save(operation);

        return mapToDTO(updatedAccount);
    }

    @Transactional
    public AccountResponseDTO debitAccount(Long accountId, TransactionRequestDTO request) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("INVALID_AMOUNT", "Amount must be greater than zero");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("INSUFFICIENT_BALANCE", "Insufficient balance for withdrawal");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        // Record operation
        Operation operation = Operation.builder()
            .accountId(accountId)
            .operationType(AppConstants.OPERATION_TYPE_DEBIT)
            .amount(request.getAmount())
            .description(request.getDescription())
            .build();
        operationRepository.save(operation);

        return mapToDTO(updatedAccount);
    }

    @Transactional
    public void transferMoney(TransferRequestDTO request) {
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account toAccount = accountRepository.findById(request.getToAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("INVALID_AMOUNT", "Transfer amount must be greater than zero");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("INSUFFICIENT_BALANCE", "Insufficient balance for transfer");
        }

        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        accountRepository.save(fromAccount);

        // Credit to destination account
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        accountRepository.save(toAccount);

        // Record operations
        Operation debitOp = Operation.builder()
            .accountId(fromAccount.getId())
            .operationType(AppConstants.OPERATION_TYPE_TRANSFER)
            .amount(request.getAmount())
            .description("Transfer to account " + toAccount.getAccountNumber() + 
                        (request.getDescription() != null ? ": " + request.getDescription() : ""))
            .build();
        operationRepository.save(debitOp);

        Operation creditOp = Operation.builder()
            .accountId(toAccount.getId())
            .operationType(AppConstants.OPERATION_TYPE_TRANSFER)
            .amount(request.getAmount())
            .description("Transfer from account " + fromAccount.getAccountNumber() + 
                        (request.getDescription() != null ? ": " + request.getDescription() : ""))
            .build();
        operationRepository.save(creditOp);
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    private AccountResponseDTO mapToDTO(Account account) {
        return AccountResponseDTO.builder()
            .id(account.getId())
            .customerId(account.getCustomerId())
            .accountNumber(account.getAccountNumber())
            .currency(account.getCurrency())
            .balance(account.getBalance())
            .status(account.getStatus())
            .createdAt(account.getCreatedAt())
            .updatedAt(account.getUpdatedAt())
            .build();
    }
}
