package com.banking.api.controller;

import com.banking.api.dto.*;
import com.banking.api.service.AccountClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    //@PreAuthorize("hasAuthority('ROLE_VIEW_CUSTOMER_NIN')")
    @PostMapping("/nin")
    public ResponseEntity<NinResponse> ninDetails(@RequestBody NinRequest request) {
        return ResponseEntity.ok(accountClassService.nin(request));
    }


    //@PreAuthorize("hasAuthority('ROLE_UPDATE_NIN')")
    @PostMapping("/updateNin")
    public ResponseEntity<NinUpdateResponse> ninDetails(@RequestBody UpdateNinRequest request) {
        return ResponseEntity.ok(accountClassService.updatenin(request));
    }
}
