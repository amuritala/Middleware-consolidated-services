package com.banking.api.service;

import com.banking.api.dto.AuthorizeRequest;
import com.banking.api.dto.CreateTellerRequest;
import com.banking.api.dto.JnrMasterFullTemplate;
import com.banking.api.dto.MultiDeJournalRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeServiceTest {

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
        service.multiTemplate(new JnrMasterFullTemplate());
        service.createTeller(new CreateTellerRequest());
        service.authorize(new AuthorizeRequest());

        assertEquals(List.of(
                "/api/v1/multiDeJournal",
                "/api/v1/MultiJrn2",
                "/api/v1/MultiJounerV2",
                "/api/v1/CreateTeller",
                "/api/v1/Autorize"), paths);
    }
}
