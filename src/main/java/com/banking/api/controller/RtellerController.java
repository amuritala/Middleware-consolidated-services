package com.banking.api.controller;

import com.banking.api.dto.AuthorizeTransactionRequest;
import com.banking.api.dto.DebitCreditRequest;
import com.banking.api.dto.ProductRequest;
import com.banking.api.dto.ReverseTransactionRequest;
import com.banking.api.dto.RtellerResponse;
import com.banking.api.dto.TransactionQueryRequest;
import com.banking.api.service.RtellerService;
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
public class RtellerController {

    private final RtellerService rtellerService;

    @PreAuthorize("hasAuthority('ROLE_PASS_ENTRY')")
    @PostMapping("/pass-entry")
    public ResponseEntity<RtellerResponse> passEntry(@RequestBody DebitCreditRequest request) {
        return ResponseEntity.ok(rtellerService.passAccountEntry(request));
    }

    @PreAuthorize("hasAuthority('ROLE_REVERSE_TRANSACTION')")
    @PostMapping("/reverse-transaction")
    public ResponseEntity<RtellerResponse> reverseTransaction(@RequestBody ReverseTransactionRequest request) {
        return ResponseEntity.ok(rtellerService.reverseTransaction(request));
    }

    @PreAuthorize("hasAuthority('ROLE_QUERY_TRANSACTION')")
    @PostMapping("/query-transaction")
    public ResponseEntity<RtellerResponse> queryTransaction(@RequestBody TransactionQueryRequest request) {
        return ResponseEntity.ok(rtellerService.queryTransaction(request));
    }

    @PreAuthorize("hasAuthority('ROLE_QUERY_PRODUCT')")
    @PostMapping("/query-product")
    public ResponseEntity<RtellerResponse> queryProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(rtellerService.queryProduct(request));
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHORIZE_TRANSACTION')")
    @PostMapping("/authorize-transaction")
    public ResponseEntity<RtellerResponse> authorizeTransaction(
            @RequestBody AuthorizeTransactionRequest request) {
        return ResponseEntity.ok(rtellerService.authorizeTransaction(request));
    }
}
