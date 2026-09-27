package com.banking.api.service;

import com.banking.api.dto.AccountStatsResponse;
import com.banking.api.dto.AuditTrailRequest;
import com.banking.api.dto.CustomerQueryRequest;
import com.banking.api.dto.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountStatsServiceTest {

    @Test
    void delegatesStatisticsOperationsAndDeserializesBodyWithoutHeader() {
        List<String> paths = new ArrayList<>();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://account-stats")
                .exchangeFunction(request -> {
                    paths.add(request.url().getPath());
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body("""
                                    {
                                      "fcubsheader": {"msgstat": "SUCCESS"},
                                      "fcubsbody": {
                                        "cumulativeIO": {"customerno": "C001"}
                                      }
                                    }
                                    """)
                            .build());
                })
                .build();
        AccountStatsService service = new AccountStatsService(webClient);

        AccountStatsResponse customerStats = service.queryCustomerStats(
                new CustomerQueryRequest("C001", "A001", "001"));
        AccountStatsResponse auditTrail = service.queryAuditTrail(
                new AuditTrailRequest("001", "A001", "2026-01-01", "2026-01-31", "VIEW", "CHQ1"));
        AccountStatsResponse transactions = service.queryAccountTransaction(
                new TransactionRequest(BigDecimal.TEN, "A001", "001"));

        assertNotNull(customerStats.getFcubsbody());
        assertEquals("C001", customerStats.getFcubsbody().getCumulativeIO().getCustomerno());
        assertNotNull(auditTrail.getFcubsbody());
        assertNotNull(transactions.getFcubsbody());
        assertEquals(List.of(
                "/api/v1/QueryCustomerStats",
                "/api/v1/QueryAuditTrail",
                "/api/v1/QueryAccountTransaction"), paths);
    }

    @Test
    void mapsUppercaseAccountTransactionsResponse() {
        String rawResponse = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [],
                    "FCUBSWARNINGRESP": [],
                    "accDetailsFull": {
                      "ACCBRN": "101",
                      "ACCNO": "1010089970301010",
                      "NUMOFTRN": 50,
                      "accTransaction": [
                        {
                          "ACBRN": "101",
                          "ACCCY": "SLE",
                          "ACNO": "1010089970301010",
                          "DRBRIND": "C",
                          "LCYAMT": 100,
                          "MOD": "DE",
                          "REFNO": "101kjjm262870001",
                          "TRNCD": "201",
                          "TRNDT": "2026-10-13T23:00:00.000Z",
                          "VALDT": "2026-10-13T23:00:00.000Z"
                        }
                      ]
                    },
                    "accDetailsIO": null
                  },
                  "FCUBSHEADER": {
                    "MSGSTAT": "SUCCESS",
                    "OPERATION": "QueryAccTrns"
                  }
                }
                """;
        WebClient webClient = WebClient.builder()
                .baseUrl("http://account-stats")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(rawResponse)
                        .build()))
                .build();

        AccountStatsResponse response = new AccountStatsService(webClient)
                .queryAccountTransaction(new TransactionRequest(BigDecimal.TEN, "A001", "001"));

        assertEquals("SUCCESS", response.getFcubsheader().getMsgstat());
        assertEquals("1010089970301010", response.getFcubsbody().getAccDetailsFull().get("ACCNO"));
        assertEquals(50, response.getFcubsbody().getAccDetailsFull().get("NUMOFTRN"));
        List<?> transactions = (List<?>) response.getFcubsbody().getAccDetailsFull().get("accTransaction");
        assertEquals("101kjjm262870001", ((java.util.Map<?, ?>) transactions.get(0)).get("REFNO"));
    }

    @Test
    void mapsUppercaseAuditTrailFailureResponse() {
        String rawResponse = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [
                      {
                        "ERROR": [
                          {"ECODE": "ST-SAVE-024", "EDESC": "Failed to Query Data"},
                          {"ECODE": "ST-TD-004", "EDESC": "Queried Account No is not a TD account"}
                        ]
                      }
                    ],
                    "FCUBSWARNINGRESP": [],
                    "acvwAcdaudtrFull": null,
                    "acvwAcdaudtrIO": {
                      "ACTION": null,
                      "BRANCHCODE": "101",
                      "CUSTACNO": "1010089970301010",
                      "TRNFROMDT": "2026-09-30T23:00:00.000Z",
                      "TRNTODT": "2026-10-13T23:00:00.000Z"
                    }
                  },
                  "FCUBSHEADER": {
                    "MSGSTAT": "FAILURE",
                    "OPERATION": "QueryAudittrail"
                  }
                }
                """;
        WebClient webClient = WebClient.builder()
                .baseUrl("http://account-stats")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(rawResponse)
                        .build()))
                .build();

        AccountStatsResponse response = new AccountStatsService(webClient)
                .queryAuditTrail(new AuditTrailRequest("101", "1010089970301010",
                        "2026-09-30", "2026-10-13", "VIEW", "CHQ1"));

        assertEquals("FAILURE", response.getFcubsheader().getMsgstat());
        assertEquals("1010089970301010", response.getFcubsbody().getAcvwAcdaudtrIO().get("CUSTACNO"));
        assertEquals("ST-TD-004", response.getFcubsbody().getFcubserrorresp().get(0)
                .getError().get(1).getEcode());
    }
}
