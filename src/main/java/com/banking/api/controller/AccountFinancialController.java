package com.banking.api.controller;

import com.banking.api.dto.AccountStatementRequest;
import com.banking.api.dto.AccountStatementResponse;
import com.banking.api.service.AccountFinancialService;
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
public class AccountFinancialController {

    private final AccountFinancialService accountFinancialService;

    @PreAuthorize("hasAuthority('ROLE_VIEW_CUSTOMER_STATEMENT')")
    @PostMapping("/customer-statement")
    public ResponseEntity<AccountStatementResponse> queryCustomerStatement(
            @RequestBody AccountStatementRequest request) {
        return ResponseEntity.ok(accountFinancialService.queryCustomerStatement(request));
    }
}
