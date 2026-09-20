package com.banking.api.service;

import com.banking.api.dto.AccountStatsResponse;
import com.banking.api.dto.AuditTrailRequest;
import com.banking.api.dto.CustomerQueryRequest;
import com.banking.api.dto.TransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AccountStatsService {

    private final WebClient webClient;

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
                .bodyToMono(AccountStatsResponse.class)
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }
}
