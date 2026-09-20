package com.banking.api.service;

import com.banking.api.dto.StatChangeRequest;
import com.banking.api.dto.StatusChangeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AccountStatusService {

    private final WebClient webClient;

    public AccountStatusService(@Qualifier("bstServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public StatusChangeResponse changeAccountStatus(StatChangeRequest request) {
        return webClient.post()
                .uri("api/v1/AccountStatusChange")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StatusChangeResponse.class)
                .doOnError(ex -> log.error("Changing account status failed: {}", ex.getMessage()))
                .block();
    }
}
