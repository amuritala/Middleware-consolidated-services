package com.banking.api.controller;

import com.banking.api.dto.StatChangeRequest;
import com.banking.api.dto.StatusChangeResponse;
import com.banking.api.service.AccountStatusService;
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
public class AccountStatusController {

    private final AccountStatusService accountStatusService;

    @PreAuthorize("hasAuthority('ROLE_CHANGE_ACCOUNT_STATUS')")
    @PostMapping("/account-status-change")
    public ResponseEntity<StatusChangeResponse> changeAccountStatus(@RequestBody StatChangeRequest request) {
        return ResponseEntity.ok(accountStatusService.changeAccountStatus(request));
    }
}
