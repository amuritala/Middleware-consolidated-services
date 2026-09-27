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

    @Test
    void mapsUppercaseQueryImageResponseAndSignatureDetails() {
        String imageText = "base64-signature-data";
        String rawResponse = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [],
                    "FCUBSWARNINGRESP": [
                      {
                        "WARNING": [
                          {"WCODE": "ST-SAVE-073", "WDESC": "Successfully Retrieved"}
                        ]
                      }
                    ],
                    "svvwsSifsigmasterFull": {
                      "AUTHSTAT": "A",
                      "BRANCH": "100",
                      "CUSTOMERNAME": "HAMDE LOGIST AND GEN SUPP ENTPP",
                      "CUSTOMERNUMBER": "008997",
                      "SIGID": "008997",
                      "SIGNNAME1": "HAMDE LOGISTICS AND GEN SUPPLIES EN",
                      "SIGTITLE": "303756",
                      "TXNSTAT": "O",
                      "svvwsSifsigdetail": [
                        {
                          "IMAGENAME": "303756_008997_2.jpg",
                          "IMAGETEXT": "%s",
                          "IMAGETYPE": "I",
                          "SEQSPECNUMBER": 1
                        }
                      ]
                    },
                    "svvwsSifsigmasterIO": null
                  },
                  "FCUBSHEADER": {"MSGSTAT": "SUCCESS", "OPERATION": "QueryFCUBSSigService"}
                }
                """.formatted(imageText);
        WebClient webClient = WebClient.builder()
                .baseUrl("http://image-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(rawResponse)
                        .build()))
                .build();

        var response = new ImageService(webClient).queryImage(new ImageSignatureRequest());

        assertEquals("SUCCESS", response.getFcubsheader().getMsgstat());
        assertEquals("008997", response.getFcubsbody().getSvvwsSifsigmasterFull().getSigid());
        assertEquals("HAMDE LOGIST AND GEN SUPP ENTPP",
                response.getFcubsbody().getSvvwsSifsigmasterFull().getCustomername());
        assertEquals(imageText, response.getFcubsbody().getSvvwsSifsigmasterFull()
                .getSvvwsSifsigdetail().get(0).getImagetext());
        assertEquals("303756_008997_2.jpg", response.getFcubsbody().getSvvwsSifsigmasterFull()
                .getSvvwsSifsigdetail().get(0).getImagename());
    }
}
