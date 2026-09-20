package com.banking.api.service;

import com.banking.api.dto.AccountStatementRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountFinancialServiceTest {

    @Test
    void queriesCustomerStatementAndDeserializesBodyWithoutHeader() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://account-fin")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                {
                                  "fcubsheader": {"msgstat": "SUCCESS"},
                                  "fcubsbody": {
                                    "custAccStmtAdhocRequest": {
                                      "xref": "X001",
                                      "dcn": "D001"
                                    }
                                  }
                                }
                                """)
                        .build()))
                .build();
        AccountFinancialService service = new AccountFinancialService(webClient);

        var response = service.queryCustomerStatement(new AccountStatementRequest(
                "X001", "A001", "001", "2026-01-01T00:00:00",
                "2026-01-31T23:59:59", "S", "B", "N"));

        assertNotNull(response.getFcubsbody());
        assertEquals("X001", response.getFcubsbody().getCustAccStmtAdhocRequest().getXref());
        assertEquals("D001", response.getFcubsbody().getCustAccStmtAdhocRequest().getDcn());
    }
}
