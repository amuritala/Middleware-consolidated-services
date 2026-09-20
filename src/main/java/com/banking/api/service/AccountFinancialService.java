package com.banking.api.service;

import com.banking.api.dto.AccountStatementRequest;
import com.banking.api.dto.AccountStatementResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AccountFinancialService {

    private final WebClient webClient;

    public AccountFinancialService(@Qualifier("accountFinServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public AccountStatementResponse queryCustomerStatement(AccountStatementRequest request) {
        return webClient
                .post()
                .uri("api/v1/QueryCustomerStatement")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AccountStatementResponse.class)
                .doOnError(ex -> log.error("Querying customer statement failed: {}", ex.getMessage()))
                .block();
    }
}
