package com.banking.api.service;

import com.banking.api.dto.AccountBalanceRequest;
import com.banking.api.dto.AccountDetailsRequest;
import com.banking.api.dto.AccountNumberRequest;
import com.banking.api.dto.AccountCreationRequest;
import com.banking.api.dto.StatementRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountServiceTest {

    @Test
    void mapsUppercaseCreateAccountEnvelopeAndMessageStatus() {
        String responseJson = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [
                      {
                        "ERROR": [
                          {
                            "ECODE": "PC-CUA-004",
                            "EDESC": "Customer Account Number cannot be blank"
                          }
                        ]
                      }
                    ],
                    "FCUBSWARNINGRESP": [],
                    "custAccountFull": {
                      "ACC": null,
                      "ACCLS": "STSVEI",
                      "BRN": "101",
                      "CCY": "SLE",
                      "CUSTNO": "054855"
                    }
                  },
                  "FCUBSHEADER": {
                    "MSGSTAT": "FAILURE",
                    "ACTION": "NEW",
                    "SERVICE": "FCUBSAccService"
                  }
                }
                """;
        WebClient webClient = WebClient.builder()
                .baseUrl("http://account-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(responseJson)
                        .build()))
                .build();

        AccountCreationRequest request = new AccountCreationRequest("101", "123456", "054855", "SLE", "STSVEI");
        var response = new AccountService(
                webClient,
                new ObjectMapper().findAndRegisterModules()
        ).createAccount(request);

        assertEquals("FAILURE", response.getFcubsheader().getMsgstat());
        assertEquals("STSVEI", response.getFcubsbody().getCustAccountFull().get("ACCLS"));
        assertEquals("PC-CUA-004", response.getFcubsbody().getFcubserrorresp().get(0)
                .getError().get(0).getEcode());
        assertEquals(16, request.getAcc().length());
        assertEquals("101054855", request.getAcc().substring(0, 9));
        assertEquals(7, request.getAcc().substring(9).length());
    }

    @Test
    void delegatesAccountOperationsToExpectedEndpoints() {
        List<String> paths = new ArrayList<>();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://account-service")
                .exchangeFunction(request -> {
                    paths.add(request.url().getPath());
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body("{\"fcubsheader\":{\"msgstat\":\"SUCCESS\"},\"fcubsbody\":{}}")
                            .build());
                })
                .build();
        AccountService accountService = new AccountService(
                webClient,
                new ObjectMapper().findAndRegisterModules()
        );
        AccountBalanceRequest balanceRequest = new AccountBalanceRequest("001", "123456");
        AccountNumberRequest accountNumberRequest = new AccountNumberRequest("123456");

        var summaryBalance = accountService.summaryBalance(accountNumberRequest);
        assertNotNull(summaryBalance.getFcubsbody());
        assertEquals("SUCCESS", summaryBalance.getFcubsheader().getMsgstat());
        assertNotNull(accountService.fullAccountBalance(accountNumberRequest).getFcubsbody());
        assertNotNull(accountService.checkout(accountNumberRequest).getFcubsbody());
        assertNotNull(accountService.accountDetails(
                new AccountDetailsRequest("001", "123456")).getFcubsbody());
        assertNotNull(accountService.statement(
                new StatementRequest("987654", "statement-1")).getFcubsbody());
        assertNotNull(accountService.checkBalance(balanceRequest).getFcubsbody());
        AccountCreationRequest createRequest =
                new AccountCreationRequest("001", "123456", "987654", "NGN", "SAV");
        assertNotNull(accountService.createAccount(createRequest).getFcubsbody());
        assertEquals(16, createRequest.getAcc().length());

        assertEquals(List.of(
                "/api/v1/Summarybal",
                "/api/v1/fullAccbal",
                "/api/v1/checkout",
                "/api/v1/AccDetails",
                "/api/v1/Statement",
                "/api/v1/bal",
                "/api/v1/createAcc"), paths);
    }
}
