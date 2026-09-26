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
    void mapsTypedJournalTransactionDetails() throws Exception {
        String requestJson = """
                {
                  "detbsJrnlTxnDetail": [
                    {
                      "serialno": 1,
                      "userrefno": "45546",
                      "drcr": "D",
                      "branchcode": "103",
                      "accorgl": "G",
                      "ccy": "SLE",
                      "amount": 100,
                      "txncode": "201",
                      "instrumentno": "",
                      "lcyamount": 100,
                      "addltext": "",
                      "acdesc": "",
                      "customer": "",
                      "exchrate": 1,
                      "account": "150320368"
                    },
                    {
                      "serialno": 2,
                      "userrefno": "45546",
                      "drcr": "C",
                      "branchcode": "103",
                      "accorgl": "A",
                      "ccy": "SLE",
                      "amount": 100,
                      "txncode": "201",
                      "instrumentno": "",
                      "lcyamount": 100,
                      "addltext": "",
                      "acdesc": "",
                      "customer": "",
                      "exchrate": 1,
                      "account": "1030046420801014"
                    }
                  ]
                }
                """;

        MultiDeJournalRequest request = new com.fasterxml.jackson.databind.ObjectMapper()
                .findAndRegisterModules()
                .readValue(requestJson, MultiDeJournalRequest.class);

        assertEquals(2, request.getDetbsJrnlTxnDetail().size());
        assertEquals(1, request.getDetbsJrnlTxnDetail().get(0).getSerialno());
        assertEquals("D", request.getDetbsJrnlTxnDetail().get(0).getDrcr());
        assertEquals("150320368", request.getDetbsJrnlTxnDetail().get(0).getAccount());
        assertEquals(0, request.getDetbsJrnlTxnDetail().get(1).getExchrate()
                .compareTo(new java.math.BigDecimal("1")));
        assertEquals("1030046420801014", request.getDetbsJrnlTxnDetail().get(1).getAccount());
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

    @Test
    void mapsUppercaseSingleDebitCreditJournalSuccessResponse() {
        String rawResponse = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [],
                    "FCUBSWARNINGRESP": [
                      {"WARNING": [{"WCODE": "ST-SAVE-052", "WDESC": "Successfully Saved and Authorized"}]}
                    ],
                    "detbsJrnlTxnMasterFull": {
                      "AUTHSTAT": "U",
                      "BATCHNO": "kjkj",
                      "BRANCHCODE": "103",
                      "CCY": "SLE",
                      "CHECHKERID": "TAKEON02",
                      "CURRNO": 1,
                      "MAKER": "TAKEON02",
                      "RECNO": 1,
                      "REFERENCENO": "103kjkj262870001",
                      "TOTALCR": 100,
                      "TOTALDR": 100,
                      "TOTALNO": 1,
                      "TXNSTAT": "A",
                      "VALUEDATE": "2026-10-13T23:00:00.000Z",
                      "detbsBatchMaster": {
                        "BATCHNO": "kjkj",
                        "CREDIT": 100,
                        "CRENTTOTAL": 100,
                        "DEBIT": 100,
                        "DRENTTOTAL": 100,
                        "DESCRIPTION": "pass entry test"
                      },
                      "detbsJrnlTxnDetail": [
                        {
                          "ACCORGL": "G",
                          "ACCOUNT": "150320368",
                          "AMOUNT": 100,
                          "BRANCHCODE": "103",
                          "CCY": "SLE",
                          "DRCR": "D",
                          "EXCHRATE": 1,
                          "LCYAMOUNT": 100,
                          "SERIALNO": 1,
                          "TXNCODE": "201",
                          "USERREFNO": "45546"
                        }
                      ],
                      "devwsBatchMaster": null,
                      "misdetails": {"CALCMETH1": "1"}
                    }
                  },
                  "FCUBSHEADER": {
                    "MSGSTAT": "SUCCESS",
                    "OPERATION": "CreateMjrnlbook"
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

        DeResponse response = new DeService(webClient).multiJournal2(new MultiDeJournalRequest());

        MultiJrnlBookFull full = response.getFcubsbody().getDetbsJrnlTxnMasterFull();
        assertEquals("SUCCESS", response.getFcubsheader().getMsgstat());
        assertEquals("103kjkj262870001", full.getReferenceno());
        assertEquals(0, full.getTotaldr().compareTo(new java.math.BigDecimal("100")));
        assertEquals("kjkj", full.getDetbsBatchMaster().getBatchno());
        assertEquals("150320368", full.getDetbsJrnlTxnDetail().get(0).getAccount());
        assertEquals("1", full.getMisdetails().get("CALCMETH1").asText());
    }
}
