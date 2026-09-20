package com.banking.api.controller;

import com.banking.api.dto.AccountBalanceRequest;
import com.banking.api.dto.AccountCreationRequest;
import com.banking.api.dto.AccountDetailsRequest;
import com.banking.api.dto.AccountNumberRequest;
import com.banking.api.dto.AccountResponse;
import com.banking.api.dto.CreateAccountResponse;
import com.banking.api.dto.FullAccountBalanceResponse;
import com.banking.api.dto.StatementRequest;
import com.banking.api.dto.StatementResponse;
import com.banking.api.dto.SummaryBalanceResponse;
import com.banking.api.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
@SecurityRequirement(name = "keycloak")
@PreAuthorize("hasAuthority('ROLE_CLIENT')")
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasAuthority('ROLE_CREATE_ACCOUNT')")
    @PostMapping("/create-account")
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody AccountCreationRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PreAuthorize("hasAuthority('ROLE_CHECK_BALANCE')")
    @PostMapping("/check-balance")
    public ResponseEntity<AccountResponse> checkBalance(@RequestBody AccountBalanceRequest request) {
        return ResponseEntity.ok(accountService.checkBalance(request));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_SUMMARY_BALANCE')")
    @PostMapping("/summary-balance")
    public ResponseEntity<SummaryBalanceResponse> summaryBalance(@RequestBody AccountNumberRequest request) {
        return ResponseEntity.ok(accountService.summaryBalance(request));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_FULL_ACCOUNT_BALANCE')")
    @PostMapping("/full-account-balance")
    public ResponseEntity<FullAccountBalanceResponse> fullAccountBalance(@RequestBody AccountNumberRequest request) {
        return ResponseEntity.ok(accountService.fullAccountBalance(request));
    }

    @PreAuthorize("hasAuthority('ROLE_CHECKOUT_ACCOUNT')")
    @PostMapping("/checkout")
    public ResponseEntity<AccountResponse> checkout(@RequestBody AccountNumberRequest request) {
        return ResponseEntity.ok(accountService.checkout(request));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_ACCOUNT_DETAILS')")
    @PostMapping("/account-details")
    public ResponseEntity<AccountResponse> accountDetails(@RequestBody AccountDetailsRequest request) {
        return ResponseEntity.ok(accountService.accountDetails(request));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_ACCOUNT_STATEMENT')")
    @PostMapping("/statement")
    public ResponseEntity<StatementResponse> statement(@RequestBody StatementRequest request) {
        return ResponseEntity.ok(accountService.statement(request));
    }
}
