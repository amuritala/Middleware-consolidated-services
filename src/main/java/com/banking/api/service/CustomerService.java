package com.banking.api.service;

import com.banking.api.dto.CustomerCreateRequest;
import com.banking.api.dto.AmtBlockNoRequest;
import com.banking.api.dto.AmountBlockRequest;
import com.banking.api.dto.CreateCorporateRequest;
import com.banking.api.dto.CustomerAccountDetailsRequest;
import com.banking.api.dto.CustomerResponse;
import com.banking.api.dto.CustomerNumberRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
public class CustomerService {

    private final WebClient webClient;

    public CustomerService(@Qualifier("customerWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public CustomerResponse createCustomer(CustomerCreateRequest request) {
        log.info("Creating customer with fullname: {}", request.getFullname());
        return post("/api/v1/createcust", request, "creating customer");
    }

    public CustomerResponse queryCustomer(CustomerNumberRequest request) {
        return post("/api/v1/QueryCustomer", request, "querying customer");
    }

    public CustomerResponse queryAmountBlock(AmtBlockNoRequest request) {
        return post("/api/v1/QueryAmtBlk", request, "querying amount block");
    }

    public CustomerResponse createJointCustomer(CreateCorporateRequest request) {
        return post("/api/v1/CreateCustomerJoint", request, "creating joint customer");
    }

    public CustomerResponse createCorporateCustomer(CreateCorporateRequest request) {
        return post("/api/v1/CreateCustomerCorp", request, "creating corporate customer");
    }

    public CustomerResponse amountBlock(AmountBlockRequest request) {
        return post("/api/v1/AmtBlock", request, "creating amount block");
    }

    public CustomerResponse accountDetails(CustomerAccountDetailsRequest request) {
        return post("/api/v1/AccDetails", request, "querying customer account details");
    }

    private CustomerResponse post(String uri, Object request, String operation) {
        return webClient.post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }

}
