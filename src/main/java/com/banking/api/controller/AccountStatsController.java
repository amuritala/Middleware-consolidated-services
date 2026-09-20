package com.banking.api.controller;

import com.banking.api.dto.AccountStatsResponse;
import com.banking.api.dto.AuditTrailRequest;
import com.banking.api.dto.CustomerQueryRequest;
import com.banking.api.dto.TransactionRequest;
import com.banking.api.service.AccountStatsService;
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
public class AccountStatsController {

    private final AccountStatsService accountStatsService;

    @PreAuthorize("hasAuthority('ROLE_VIEW_CUSTOMER_STATS')")
    @PostMapping("/customer-stats")
    public ResponseEntity<AccountStatsResponse> queryCustomerStats(@RequestBody CustomerQueryRequest request) {
        return ResponseEntity.ok(accountStatsService.queryCustomerStats(request));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_AUDIT_TRAIL')")
    @PostMapping("/audit-trail")
    public ResponseEntity<AccountStatsResponse> queryAuditTrail(@RequestBody AuditTrailRequest request) {
        return ResponseEntity.ok(accountStatsService.queryAuditTrail(request));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_ACCOUNT_TRANSACTIONS')")
    @PostMapping("/account-transactions")
    public ResponseEntity<AccountStatsResponse> queryAccountTransaction(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(accountStatsService.queryAccountTransaction(request));
    }
}
