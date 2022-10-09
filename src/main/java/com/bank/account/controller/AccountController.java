package com.bank.account.controller;

import com.bank.account.dto.*;
import com.bank.account.service.AccountService;
import com.bank.account.service.OperationService;
import com.bank.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationService operationService;

    @PostMapping("/create")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> createAccount(@RequestBody AccountRequestDTO request) {
        AccountResponseDTO account = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(account, "Account created successfully"));
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> getAccount(@PathVariable Long id) {
        AccountResponseDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(account));
    }

    @GetMapping("/customer/{customerId}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<AccountResponseDTO>>> getAccountsByCustomer(@PathVariable Long customerId) {
        List<AccountResponseDTO> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequestDTO request) {
        AccountResponseDTO account = accountService.updateAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success(account, "Account updated successfully"));
    }

    @PostMapping("/{id}/credit")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> creditAccount(
            @PathVariable Long id,
            @RequestBody TransactionRequestDTO request) {
        AccountResponseDTO account = accountService.creditAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success(account, "Credit transaction successful"));
    }

    @PostMapping("/{id}/debit")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> debitAccount(
            @PathVariable Long id,
            @RequestBody TransactionRequestDTO request) {
        AccountResponseDTO account = accountService.debitAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success(account, "Debit transaction successful"));
    }

    @PostMapping("/transfer")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> transferMoney(@RequestBody TransferRequestDTO request) {
        accountService.transferMoney(request);
        return ResponseEntity.ok(ApiResponse.success("Transfer completed successfully"));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Account deleted successfully"));
    }

    @GetMapping("/{accountId}/operations")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<OperationResponseDTO>>> getOperations(@PathVariable Long accountId) {
        List<OperationResponseDTO> operations = operationService.getOperationsByAccountId(accountId);
        return ResponseEntity.ok(ApiResponse.success(operations));
    }

    @GetMapping("/operation/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<OperationResponseDTO>> getOperation(@PathVariable Long id) {
        OperationResponseDTO operation = operationService.getOperationById(id);
        return ResponseEntity.ok(ApiResponse.success(operation));
    }
}
