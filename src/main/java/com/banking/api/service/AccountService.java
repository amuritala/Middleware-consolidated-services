package com.banking.api.service;

import com.banking.api.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.SecureRandom;
import java.util.Objects;

@Service
@Slf4j
public class AccountService {
    public String serviceName = "Account Service";
    private static final SecureRandom ACCOUNT_NUMBER_RANDOM = new SecureRandom();

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AccountService(
            @Qualifier("accountServiceWebClient") WebClient webClient,
            ObjectMapper objectMapper
    ) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public CreateAccountResponse createAccount(AccountCreationRequest request) {
        Objects.requireNonNull(request, "Account creation request cannot be null");
        request.setAcc(generateAccountNumber(request.getBrn(), request.getCustno()));
        log.info("Creating account for customer number {}", request.getCustno());
        return postAndLogRawResponse(
                "api/v1/createAcc",
                request,
                CreateAccountResponse.class,
                "creating account"
        );
    }

    private String generateAccountNumber(String branchCode, String customerNumber) {
        Objects.requireNonNull(branchCode, "Branch code cannot be null");
        Objects.requireNonNull(customerNumber, "Customer number cannot be null");
        return branchCode + customerNumber
                + String.format("%07d", ACCOUNT_NUMBER_RANDOM.nextInt(10_000_000));
    }

    public AccountResponse checkBalance(AccountBalanceRequest request) {
        log.info("Checking balance for custacno {}", request.getCustacno());
        return post("api/v1/bal", request, AccountResponse.class, "checking account balance");
    }

    public SummaryBalanceResponse summaryBalance(AccountNumberRequest request) {
        log.info("Getting summary balance for custacno {}", request.getCustacno());
        return post("api/v1/Summarybal", request, SummaryBalanceResponse.class, "getting summary balance");
    }

    public FullAccountBalanceResponse fullAccountBalance(AccountNumberRequest request) {
        log.info("Getting full account balance for custacno {}", request.getCustacno());
        return post("api/v1/fullAccbal", request, FullAccountBalanceResponse.class, "getting full account balance");
    }

    public AccountResponse requestChequeBook(ChequeBookRequest request) {
        log.info("Requesting cheque book for account {}", request.getAccount());
        return post("api/v1/checkbook", request, AccountResponse.class, "requesting cheque book");
    }

    public AccountResponse accountDetails(AccountDetailsRequest request) {
        log.info("Getting account details for custacno {}", request.getCustacno());
        return post("api/v1/AccDetails", request, AccountResponse.class, "getting account details");
    }

    public StatementResponse statement(StatementRequest request) {
        log.info("Getting statement for customer number {}", request.getCusno());
        return post("api/v1/Statement", request, StatementResponse.class, "getting account statement");
    }


    public CreateStopPaymentResponse stoppayment(StopPaymentRequest request) {
        log.info("Requesting stoppayment for account {}", request.getAccnumber() );
        return post("api/v1/StopPayment", request, CreateStopPaymentResponse.class, "requesting for stoppayment");
    }


    public AccountClassTransferResponse accountclasstransfer(AccountClassTransferRequest request) {
        log.info("Requesting account class transfer for account {}", request.getAcc() );
        return post("api/v1/AccountClassTransfer", request, AccountClassTransferResponse.class, "requesting for account class transfer");
    }

    public CreateTdAccountResponse createtdaccount(TdAccountRequest request) {
        log.info("creating term deposit account for customer {}", request.getCustno() );
        return post("api/v1/CreateTdAccount", request, CreateTdAccountResponse.class, "creating termed deposit account");
    }


    private <T> T post(String uri, Object request, Class<T> responseType, String operation) {
        return webClient
                .post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for {}: {}", operation, rawResponse))
                .map(rawResponse -> {
                    try {
                        return objectMapper.readValue(rawResponse, responseType);
                    } catch (JsonProcessingException exception) {
                        throw new IllegalStateException(
                                "Unable to deserialize raw response for " + operation,
                                exception
                        );
                    }
                })
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }

    private <T> T postAndLogRawResponse(
            String uri,
            Object request,
            Class<T> responseType,
            String operation
    ) {
        return webClient
                .post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for {}: {}", operation, rawResponse))
                .map(rawResponse -> {
                    try {
                        return objectMapper.readValue(rawResponse, responseType);
                    } catch (JsonProcessingException exception) {
                        throw new IllegalStateException(
                                "Unable to deserialize raw response for " + operation,
                                exception
                        );
                    }
                })
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }
}
