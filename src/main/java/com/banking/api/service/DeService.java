package com.banking.api.service;

import com.banking.api.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class DeService {
    public String serviceName = "De Service";
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public DeService(@Qualifier("deServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public DeResponse multiDeJournal(MultiDeJournalRequest request) {
        return post("api/v1/multiDeJournalBulkDebitCredit", request, "creating multi journal");
    }

    public DeResponse multiJournal2(MultiDeJournalRequest request) {
        return post("api/v1/DeJrnSingleDebitCredit", request, "creating multi journal v2");
    }

    public DeResponse reverseJournal(ReversalRequest request) {
        return post("api/v1/ReverseJrn", request, "creating journal template");
    }

    public DeResponse queryJournal(QueryRequest request) {
        return post("api/v1/QueryMultiJrn", request, "creating teller transaction");
    }

    public DeResponse authorize(AuthorizeRequest request) {
        return post("api/v1/Autorize", request, "authorizing journal");
    }

    private DeResponse post(String uri, Object request, String operation) {
        return webClient.post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for {}: {}", operation, rawResponse))
                .map(rawResponse -> readResponse(rawResponse, operation))
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }

    private DeResponse readResponse(String rawResponse, String operation) {
        try {
            return objectMapper.readValue(rawResponse, DeResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for " + operation, exception);
        }
    }
}
