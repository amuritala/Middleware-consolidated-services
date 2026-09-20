package com.banking.api.service;

import com.banking.api.dto.DebitCreditRequest;
import com.banking.api.dto.AuthorizeTransactionRequest;
import com.banking.api.dto.ProductRequest;
import com.banking.api.dto.ReverseTransactionRequest;
import com.banking.api.dto.RtellerResponse;
import com.banking.api.dto.TransactionQueryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RtellerService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    public RtellerService(@Qualifier("rtellerServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public RtellerResponse passAccountEntry(DebitCreditRequest request) {
        log.info("Sending request for the batch no: {}", request.getBatchno());
        return post("api/v1/PassAccounting", request, "passing account entry");
    }

    public RtellerResponse reverseTransaction(ReverseTransactionRequest request) {
        return post("api/v1/ReserveTrasactiom", request, "reversing transaction");
    }

    public RtellerResponse queryTransaction(TransactionQueryRequest request) {
        return post("api/v1/QueryTrasactiom", request, "querying transaction");
    }

    public RtellerResponse queryProduct(ProductRequest request) {
        return post("api/v1/QueryProduct", request, "querying product");
    }

    public RtellerResponse authorizeTransaction(AuthorizeTransactionRequest request) {
        return post("api/v1/AutorizeTrasactiom", request, "authorizing transaction");
    }

    private RtellerResponse post(String uri, Object request, String operation) {
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

    private RtellerResponse readResponse(String rawResponse, String operation) {
        try {
            return objectMapper.readValue(rawResponse, RtellerResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for " + operation, exception);
        }
    }
}
