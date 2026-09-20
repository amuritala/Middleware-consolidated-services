package com.banking.api.controller;

import com.banking.api.dto.AccountClass;
import com.banking.api.dto.CustomerCategory;
import com.banking.api.dto.Location;
import com.banking.api.service.AccountClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/service/account-class")
@RequiredArgsConstructor
@SecurityRequirement(name = "keycloak")
@PreAuthorize("hasAuthority('ROLE_CLIENT')")
public class AccountClassController {

    private final AccountClassService accountClassService;

    @PreAuthorize("hasAuthority('ROLE_VIEW_ACCOUNT_CLASS_DATA')")
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(accountClassService.getAllLocations());
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_ACCOUNT_CLASS_DATA')")
    @GetMapping("/customer-categories")
    public ResponseEntity<List<CustomerCategory>> getAllCustomerCategories() {
        return ResponseEntity.ok(accountClassService.getAllCustomerCategories());
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_ACCOUNT_CLASS_DATA')")
    @GetMapping("/classes")
    public ResponseEntity<List<AccountClass>> getAllAccountClasses() {
        return ResponseEntity.ok(accountClassService.getAllAccountClasses());
    }
}
