package com.banking.api.service;

import com.banking.api.dto.ImageSignatureRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageServiceTest {

    @Test
    void queriesImageAndDeserializesBodyWithoutHeader() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://image-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                {
                                  "fcubsheader": {"msgstat": "SUCCESS"},
                                  "fcubsbody": {
                                    "svvwsSifsigmasterFull": {
                                      "customernumber": "C001",
                                      "sigid": "S001"
                                    }
                                  }
                                }
                                """)
                        .build()))
                .build();

        var response = new ImageService(webClient)
                .queryImage(new ImageSignatureRequest());

        assertNotNull(response.getFcubsbody());
        assertEquals("C001", response.getFcubsbody().getSvvwsSifsigmasterFull().getCustomernumber());
        assertEquals("S001", response.getFcubsbody().getSvvwsSifsigmasterFull().getSigid());
    }
}
