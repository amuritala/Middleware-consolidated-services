package com.banking.api.service;

import com.banking.api.dto.AuthRequest;
import com.banking.api.dto.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceTest {

    @Test
    void exchangesCredentialsForKeycloakAccessAndRefreshTokens() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(request -> {
                    assertEquals("/realms/banking/protocol/openid-connect/token",
                            request.url().getPath());
                    assertEquals(MediaType.APPLICATION_FORM_URLENCODED,
                            request.headers().getContentType());
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body("""
                                    {
                                      "access_token": "access-value",
                                      "refresh_token": "refresh-value",
                                      "expires_in": 300
                                    }
                                    """)
                            .build());
                });
        AuthService service = new AuthService(
                webClientBuilder, "http://keycloak", "banking", "banking-service-api", "client-secret");
        AuthRequest request = new AuthRequest();
        request.setUsername("customer");
        request.setPassword("password");

        AuthResponse response = service.login(request);

        assertEquals("access-value", response.getAccessToken());
        assertEquals("refresh-value", response.getRefreshToken());
    }

    @Test
    void returnsUnauthorizedWhenKeycloakRejectsCredentials() {
        AuthService service = new AuthService(
                WebClient.builder().exchangeFunction(request -> Mono.just(
                        ClientResponse.create(HttpStatus.UNAUTHORIZED)
                                .header("Content-Type", "application/json")
                                .body("{\"error\":\"invalid_grant\"}")
                                .build())),
                "http://keycloak", "banking", "banking-service-api", "client-secret");
        AuthRequest request = new AuthRequest();
        request.setUsername("customer");
        request.setPassword("incorrect");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> service.login(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}
