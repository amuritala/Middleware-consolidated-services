package com.banking.api.service;

import com.banking.api.dto.StatChangeRequest;
import com.banking.api.dto.StatusChangeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountStatusServiceTest {

    @Test
    void delegatesAccountStatusChangeAndDeserializesBody() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://bst-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                {
                                  "fcubsheader": {"msgstat": "SUCCESS"},
                                  "fcubsbody": {
                                    "accStatMasterFull": {"custid": "C001"}
                                  }
                                }
                                """)
                        .build()))
                .build();

        StatusChangeResponse response = new AccountStatusService(webClient)
                .changeAccountStatus(new StatChangeRequest());

        assertNotNull(response.getFcubsbody());
        assertNotNull(response.getFcubsbody().getAccStatMasterFull());
    }
}
