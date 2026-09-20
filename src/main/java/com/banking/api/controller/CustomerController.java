package com.banking.api.controller;

import com.banking.api.dto.AmtBlockNoRequest;
import com.banking.api.dto.AmountBlockRequest;
import com.banking.api.dto.CreateCorporateRequest;
import com.banking.api.dto.CustomerAccountDetailsRequest;
import com.banking.api.dto.CustomerCreateRequest;
import com.banking.api.dto.CustomerNumberRequest;
import com.banking.api.dto.CustomerResponse;
import com.banking.api.service.CustomerService;
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
public class CustomerController {

    private final CustomerService customerService;

    @PreAuthorize("hasAuthority('ROLE_CREATE_CUSTOMER')")
    @PostMapping("/create-customer")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerCreateRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PreAuthorize("hasAuthority('ROLE_QUERY_CUSTOMER')")
    @PostMapping("/query-customer")
    public ResponseEntity<CustomerResponse> queryCustomer(@RequestBody CustomerNumberRequest request) {
        return ResponseEntity.ok(customerService.queryCustomer(request));
    }

    @PreAuthorize("hasAuthority('ROLE_QUERY_AMOUNT_BLOCK')")
    @PostMapping("/query-amount-block")
    public ResponseEntity<CustomerResponse> queryAmountBlock(@RequestBody AmtBlockNoRequest request) {
        return ResponseEntity.ok(customerService.queryAmountBlock(request));
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE_JOINT_CUSTOMER')")
    @PostMapping("/create-joint-customer")
    public ResponseEntity<CustomerResponse> createJointCustomer(@RequestBody CreateCorporateRequest request) {
        return ResponseEntity.ok(customerService.createJointCustomer(request));
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE_CORPORATE_CUSTOMER')")
    @PostMapping("/create-corporate-customer")
    public ResponseEntity<CustomerResponse> createCorporateCustomer(@RequestBody CreateCorporateRequest request) {
        return ResponseEntity.ok(customerService.createCorporateCustomer(request));
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE_AMOUNT_BLOCK')")
    @PostMapping("/amount-block")
    public ResponseEntity<CustomerResponse> amountBlock(@RequestBody AmountBlockRequest request) {
        return ResponseEntity.ok(customerService.amountBlock(request));
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_CUSTOMER_ACCOUNT_DETAILS')")
    @PostMapping("/customer-account-details")
    public ResponseEntity<CustomerResponse> accountDetails(@RequestBody CustomerAccountDetailsRequest request) {
        return ResponseEntity.ok(customerService.accountDetails(request));
    }
}
