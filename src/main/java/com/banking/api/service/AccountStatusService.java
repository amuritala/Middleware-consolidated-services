package com.banking.api.service;

import com.banking.api.dto.StatChangeRequest;
import com.banking.api.dto.StatusChangeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AccountStatusService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public AccountStatusService(@Qualifier("bstServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public StatusChangeResponse changeAccountStatus(StatChangeRequest request) {
        return webClient.post()
                .uri("api/v1/AccountStatusChange")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for changing account status: {}", rawResponse))
                .map(this::readResponse)
                .doOnError(ex -> log.error("Changing account status failed: {}", ex.getMessage()))
                .block();
    }

    private StatusChangeResponse readResponse(String rawResponse) {
        try {
            return objectMapper.readValue(rawResponse, StatusChangeResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for changing account status",
                    exception);
        }
    }
}
