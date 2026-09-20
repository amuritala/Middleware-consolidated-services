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
}
