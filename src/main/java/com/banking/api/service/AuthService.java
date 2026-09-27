package com.banking.api.service;

import com.banking.api.dto.AuthRequest;
import com.banking.api.dto.AuthResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final WebClient webClient;
    private final String realm;
    private final String clientId;

    public AuthService(
            WebClient.Builder webClientBuilder,
            @Value("${keycloak.auth-server-url}") String authServerUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.resource}") String clientId) {
        this.webClient = webClientBuilder.baseUrl(authServerUrl).build();
        this.realm = realm;
        this.clientId = clientId;
    }

    public AuthResponse login(AuthRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("username", request.getUsername());
        form.add("password", request.getPassword());

        KeycloakTokenResponse tokenResponse = webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> Mono.error(new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Keycloak rejected the login credentials")))
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_GATEWAY, "Keycloak authentication service is unavailable")))
                .bodyToMono(KeycloakTokenResponse.class)
                .block();

        if (tokenResponse == null || tokenResponse.getAccessToken() == null
                || tokenResponse.getRefreshToken() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Keycloak returned an incomplete token response");
        }

        return new AuthResponse(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    @Data
    private static class KeycloakTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;
    }
}
