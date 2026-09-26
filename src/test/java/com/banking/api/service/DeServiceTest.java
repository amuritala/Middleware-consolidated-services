package com.banking.api.service;

import com.banking.api.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeServiceTest {

    @Test
    void mapsTypedBatchMasterObjectsInMultiDeJournalRequest() throws Exception {
        String requestJson = """
                {
                  "detbsBatchMaster": {
                    "batchno": "kjkj",
                    "description": "pass entry test with bayo and moruff ",
                    "debit": 100,
                    "credit": 100,
                    "drenttotal": 100,
                    "crenttotal": 100
                  },
                  "devwsBatchMaster": {
                    "batchnumber": "kjkj",
                    "description": "pass entry test with bayo and moruff ",
                    "debit": "100",
                    "credit": "100",
                    "balancing": "Y"
                  }
                }
                """;

        MultiDeJournalRequest request = new com.fasterxml.jackson.databind.ObjectMapper()
                .findAndRegisterModules()
                .readValue(requestJson, MultiDeJournalRequest.class);

        assertNotNull(request.getDetbsBatchMaster());
        assertEquals("kjkj", request.getDetbsBatchMaster().getBatchno());
        assertEquals(0, request.getDetbsBatchMaster().getDebit().compareTo(new java.math.BigDecimal("100")));
        assertEquals(0, request.getDetbsBatchMaster().getDrenttotal()
                .compareTo(new java.math.BigDecimal("100")));
        assertNotNull(request.getDevwsBatchMaster());
        assertEquals("kjkj", request.getDevwsBatchMaster().getBatchnumber());
        assertEquals("100", request.getDevwsBatchMaster().getDebit());
        assertEquals("Y", request.getDevwsBatchMaster().getBalancing());
    }

    @Test
    void delegatesAllDeOperationsToExpectedEndpoints() {
        List<String> paths = new ArrayList<>();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://de-service")
                .exchangeFunction(request -> {
                    paths.add(request.url().getPath());
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body("{\"fcubsbody\":{\"detbsJrnlTxnMasterFull\":{\"referenceno\":\"R1\"}}}")
                            .build());
                })
                .build();
        DeService service = new DeService(webClient);

        assertEquals("R1", service.multiDeJournal(new MultiDeJournalRequest())
                .getFcubsbody().getDetbsJrnlTxnMasterFull().getReferenceno());
        service.multiJournal2(new MultiDeJournalRequest());
        service.reverseJournal(new ReversalRequest());
        service.queryJournal(new QueryRequest());
        service.authorize(new AuthorizeRequest());

        assertEquals(List.of(
                "/api/v1/multiDeJournalBulkDebitCredit",
                "/api/v1/DeJrnSingleDebitCredit",
                "/api/v1/ReverseJrn",
                "/api/v1/QueryMultiJrn",
                "/api/v1/Autorize"), paths);
    }

    @Test
    void mapsUppercaseQueryJournalFailureResponse() {
        String rawResponse = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [
                      {
                        "ERROR": [
                          {
                            "ECODE": "GW-ROUT0003",
                            "EDESC": "No data found for the service, operation and source combination"
                          }
                        ]
                      }
                    ],
                    "FCUBSWARNINGRESP": [],
                    "detbsJrnlTxnMasterFull": null,
                    "detbsJrnlTxnMasterIO": {"REFERENCENO": "100qoqt262860001"}
                  },
                  "FCUBSHEADER": {
                    "ACTION": null,
                    "BRANCH": "100",
                    "FUNCTIONID": null,
                    "MSGSTAT": "FAILURE",
                    "OPERATION": "QueryMjrnlbook",
                    "SERVICE": "FCUBSDEService"
                  }
                }
                """;
        WebClient webClient = WebClient.builder()
                .baseUrl("http://de-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(rawResponse)
                        .build()))
                .build();

        DeResponse response = new DeService(webClient).queryJournal(new QueryRequest());

        assertEquals("FAILURE", response.getFcubsheader().getMsgstat());
        assertEquals("100qoqt262860001",
                response.getFcubsbody().getDetbsJrnlTxnMasterIO().get("REFERENCENO"));
        assertEquals("GW-ROUT0003", response.getFcubsbody().getFcubserrorresp().get(0)
                .getError().get(0).getEcode());
    }
}
