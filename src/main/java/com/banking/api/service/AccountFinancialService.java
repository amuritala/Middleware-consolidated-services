package com.banking.api.service;

import com.banking.api.dto.AccountStatementRequest;
import com.banking.api.dto.AccountStatementResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AccountFinancialService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public AccountFinancialService(@Qualifier("accountFinServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public AccountStatementResponse queryCustomerStatement(AccountStatementRequest request) {
        return webClient
                .post()
                .uri("api/v1/QueryCustomerStatement")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for querying customer statement: {}", rawResponse))
                .map(this::readResponse)
                .doOnError(ex -> log.error("Querying customer statement failed: {}", ex.getMessage()))
                .block();
    }

    private AccountStatementResponse readResponse(String rawResponse) {
        try {
            return objectMapper.readValue(rawResponse, AccountStatementResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for querying customer statement",
                    exception);
        }
    }
}
