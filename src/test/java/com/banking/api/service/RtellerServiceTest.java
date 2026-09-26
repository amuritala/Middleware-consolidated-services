package com.banking.api.service;

import com.banking.api.dto.AuthorizeTransactionRequest;
import com.banking.api.dto.ProductRequest;
import com.banking.api.dto.ReverseTransactionRequest;
import com.banking.api.dto.RtellerResponse;
import com.banking.api.dto.TransactionQueryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RtellerServiceTest {

    @Test
    void delegatesRtellerOperationsToExpectedEndpoints() {
        List<String> paths = new ArrayList<>();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://rteller")
                .exchangeFunction(request -> {
                    paths.add(request.url().getPath());
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body("{\"fcubsheader\":{\"msgstat\":\"SUCCESS\"},\"fcubsbody\":{}}")
                            .build());
                })
                .build();
        RtellerService service = new RtellerService(webClient);

        assertNotNull(service.passAccountEntry(new com.banking.api.dto.DebitCreditRequest()).getFcubsbody());
        service.reverseTransaction(new ReverseTransactionRequest());
        service.queryTransaction(new TransactionQueryRequest());
        service.queryProduct(new ProductRequest());
        service.authorizeTransaction(new AuthorizeTransactionRequest());

        assertEquals(List.of(
                "/api/v1/PassAccounting",
                "/api/v1/ReserveTrasactiom",
                "/api/v1/QueryTrasactiom",
                "/api/v1/QueryProduct",
                "/api/v1/AutorizeTrasactiom"), paths);
    }

    @Test
    void mapsUppercaseQueryProductResponse() {
        String rawResponse = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [],
                    "FCUBSWARNINGRESP": [
                      {"WARNING": [{"WCODE": "ST-SAVE-023", "WDESC": "Record Successfully Retrieved"}]}
                    ],
                    "RTProductFull": {
                      "AUTHSTAT": "A",
                      "PRDCD": "CHWL",
                      "PRDDESC": "CASH WITHDRAWAL",
                      "PRDGRP": "CASH",
                      "MAXRTVARI": 100,
                      "RTProductPreference": {"CASHGLPOST": "N", "TXNLIMIT": 5000000000}
                    },
                    "RTProductIO": null
                  },
                  "FCUBSHEADER": {"MSGSTAT": "SUCCESS", "OPERATION": "QueryRTProduct"}
                }
                """;
        WebClient webClient = WebClient.builder()
                .baseUrl("http://rteller")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(rawResponse)
                        .build()))
                .build();

        RtellerResponse response = new RtellerService(webClient)
                .queryProduct(new ProductRequest());

        assertEquals("SUCCESS", response.getFcubsheader().getMsgstat());
        assertEquals("CHWL", response.getFcubsbody().getRtProductFull().get("PRDCD"));
        assertEquals("CASH WITHDRAWAL", response.getFcubsbody().getRtProductFull().get("PRDDESC"));
        assertEquals("ST-SAVE-023", response.getFcubsbody().getFcubswarningresp().get(0)
                .getWarning().get(0).getWcode());
    }
}
