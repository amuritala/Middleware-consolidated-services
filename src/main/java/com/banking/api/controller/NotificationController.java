package com.banking.api.controller;

import com.banking.api.dto.EmailRequest;
import com.banking.api.dto.SmsBulkRequest;
import com.banking.api.dto.SmsRequest;
import com.banking.api.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "keycloak")
public class NotificationController {
    private final NotificationService notificationService;

    @PreAuthorize("hasAuthority('ROLE_CLIENT') and hasAuthority('ROLE_SEND_EMAIL')")
    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        return notificationService.sendEmail(request);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT') and hasAuthority('ROLE_SEND_SMS')")
    @PostMapping("/sms")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest request) {
        return notificationService.sendSms(request);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT') and hasAuthority('ROLE_SEND_SMS')")
    @PostMapping("/sms/bulk")
    public ResponseEntity<String> sendBulkSms(@RequestBody SmsBulkRequest request) {
        return notificationService.sendBulkSms(request);
    }
}
