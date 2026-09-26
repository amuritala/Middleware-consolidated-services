package com.banking.api.controller;

import com.banking.api.dto.*;
import com.banking.api.service.DeService;
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
public class DeController {

    private final DeService deService;

    @PreAuthorize("hasAuthority('ROLE_CREATE_DE_JOURNAL')")
    @PostMapping("/multi-de-bulk-journal")
    public ResponseEntity<DeResponse> multiDeJournal(@RequestBody MultiDeJournalRequest request) {
        return ResponseEntity.ok(deService.multiDeJournal(request));
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE_DE_JOURNAL')")
    @PostMapping("/de-single-debit-credit-journal")
    public ResponseEntity<DeResponse> multiJournal2(@RequestBody MultiDeJournalRequest request) {
        return ResponseEntity.ok(deService.multiJournal2(request));
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE_DE_TEMPLATE')")
    @PostMapping("/de-reserval")
    public ResponseEntity<DeResponse> multiTemplate(@RequestBody ReversalRequest request) {
        return ResponseEntity.ok(deService.reverseJournal(request));
    }

    @PreAuthorize("hasAuthority('ROLE_QUERY_JOURNAL')")
    @PostMapping("/query-journal")
    public ResponseEntity<DeResponse> createTeller(@RequestBody QueryRequest request) {
        return ResponseEntity.ok(deService.queryJournal(request));
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHORIZE_DE_TRANSACTION')")
    @PostMapping("/authorize")
    public ResponseEntity<DeResponse> authorize(@RequestBody AuthorizeRequest request) {
        return ResponseEntity.ok(deService.authorize(request));
    }
}
