package com.banking.api.service;

import com.banking.api.dto.AccountStatsResponse;
import com.banking.api.dto.AuditTrailRequest;
import com.banking.api.dto.CustomerQueryRequest;
import com.banking.api.dto.TransactionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AccountStatsService {
    public String serviceName = "Account Stat Service";
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public AccountStatsService(@Qualifier("accountStatsServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public AccountStatsResponse queryCustomerStats(CustomerQueryRequest request) {
        return post("api/v1/QueryCustomerStats", request, "querying customer statistics");
    }

    public AccountStatsResponse queryAuditTrail(AuditTrailRequest request) {
        return post("api/v1/QueryAuditTrail", request, "querying audit trail");
    }

    public AccountStatsResponse queryAccountTransaction(TransactionRequest request) {
        return post("api/v1/QueryAccountTransaction", request, "querying account transactions");
    }

    private AccountStatsResponse post(String uri, Object request, String operation) {
        return webClient
                .post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for {}: {}", operation, rawResponse))
                .map(rawResponse -> readResponse(rawResponse, operation))
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }

    private AccountStatsResponse readResponse(String rawResponse, String operation) {
        try {
            return objectMapper.readValue(rawResponse, AccountStatsResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for " + operation, exception);
        }
    }
}
