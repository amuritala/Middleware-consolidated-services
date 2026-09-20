package com.banking.api.controller;

import com.banking.api.dto.ImageSignatureRequest;
import com.banking.api.dto.ImageSignatureResponse;
import com.banking.api.service.ImageService;
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
public class ImageController {

    private final ImageService imageService;

    @PreAuthorize("hasAuthority('ROLE_VIEW_SIGNATURE_IMAGE')")
    @PostMapping("/query-image")
    public ResponseEntity<ImageSignatureResponse> queryImage(@RequestBody ImageSignatureRequest request) {
        return ResponseEntity.ok(imageService.queryImage(request));
    }
}
