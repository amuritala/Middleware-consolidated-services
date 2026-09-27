package com.banking.api.service;

import com.banking.api.dto.AuthRequest;
import com.banking.api.dto.AuthResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AuthService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    public AuthService(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${keycloak.auth-server-url}") String authServerUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.resource}") String clientId,
            @Value("${keycloak.credentials.secret:}") String clientSecret) {
        this.webClient = webClientBuilder.baseUrl(authServerUrl).build();
        this.objectMapper = objectMapper;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret == null ? "" : clientSecret;
    }

    public AuthResponse login(AuthRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        if (!clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }
        form.add("username", request.getUsername());
        form.add("password", request.getPassword());

        KeycloakTokenResponse tokenResponse = webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("Keycloak token endpoint returned {}", response.statusCode());
                    return response.releaseBody().thenReturn(new ResponseStatusException(
                            HttpStatus.BAD_GATEWAY, "Keycloak authentication service is unavailable"));
                })
                .bodyToMono(KeycloakTokenResponse.class)
                .onErrorMap(WebClientRequestException.class, exception -> {
                    log.error("Unable to reach the Keycloak token endpoint", exception);
                    return new ResponseStatusException(
                            HttpStatus.BAD_GATEWAY, "Unable to reach Keycloak authentication service", exception);
                })
                .onErrorMap(DecodingException.class, exception -> {
                    log.error("Keycloak returned an invalid token response", exception);
                    return new ResponseStatusException(
                            HttpStatus.BAD_GATEWAY, "Keycloak returned an invalid token response", exception);
                })
                .block();

        if (tokenResponse == null || tokenResponse.getAccessToken() == null
                || tokenResponse.getAccessToken().isBlank()
                || tokenResponse.getRefreshToken() == null
                || tokenResponse.getRefreshToken().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Keycloak returned an incomplete token response");
        }

        return new AuthResponse(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    private Mono<? extends Throwable> handleClientError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> {
                    String errorCode = readKeycloakErrorCode(body);
                    if ("invalid_grant".equals(errorCode)) {
                        log.warn("Keycloak rejected login credentials with status {}", response.statusCode());
                        return new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Invalid username or password");
                    }

                    log.error("Keycloak rejected token request with status {} and error code {}",
                            response.statusCode(), errorCode == null ? "unknown" : errorCode);
                    return new ResponseStatusException(
                            HttpStatus.BAD_GATEWAY,
                            "Keycloak rejected the token request; check client configuration and direct access grants");
                });
    }

    private String readKeycloakErrorCode(String responseBody) {
        if (responseBody.isBlank()) {
            return null;
        }
        try {
            JsonNode error = objectMapper.readTree(responseBody).get("error");
            return error != null && error.isTextual() ? error.asText() : null;
        } catch (JsonProcessingException exception) {
            log.debug("Keycloak returned a non-JSON error response");
            return null;
        }
    }

    @Data
    private static class KeycloakTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;
    }
}
