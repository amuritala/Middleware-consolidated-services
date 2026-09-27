package com.banking.api.service;

import com.banking.api.dto.StatChangeRequest;
import com.banking.api.dto.AccountStatDetailRequest;
import com.banking.api.dto.StatusChangeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void serializesAccountStatusChangeRequestToExpectedShape() throws Exception {
        AccountStatDetailRequest detail = new AccountStatDetailRequest();
        detail.setAccbrn("101");
        detail.setAcc("1010022140901014");
        detail.setAccountclass("");
        detail.setAcccurr("");
        detail.setAccdesc("");

        StatChangeRequest request = new StatChangeRequest();
        request.setRefno("");
        request.setCustid("002214");
        request.setAccountclass("");
        request.setAcccurr("");
        request.setAction("P");
        request.setRestrtype("DO");
        request.setAccStatDetail(List.of(detail));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(request));

        assertEquals("", json.get("refno").asText());
        assertEquals("002214", json.get("custid").asText());
        assertEquals("P", json.get("action").asText());
        assertEquals("DO", json.get("restrtype").asText());
        assertEquals("101", json.get("accStatDetail").get(0).get("accbrn").asText());
        assertEquals("1010022140901014", json.get("accStatDetail").get(0).get("acc").asText());
        assertEquals("", json.get("accStatDetail").get(0).get("accdesc").asText());
    }

    @Test
    void mapsUppercaseAccountStatusChangeResponse() {
        String rawResponse = """
                {
                  "FCUBSBODY": {
                    "FCUBSERRORRESP": [],
                    "FCUBSWARNINGRESP": [
                      {
                        "WARNING": [
                          {"WCODE": "ST-SAVE-002", "WDESC": "Record Successfully Saved and Authorized"}
                        ]
                      }
                    ],
                    "accStatMasterFull": {
                      "ACCCURR": "ALL",
                      "ACCOUNTCLASS": "ALL",
                      "ACTION": "P",
                      "AUTHSTAT": "A",
                      "CHECKER": "TAKEON02",
                      "CHECKERSTAMP": "2026-10-14 09:22:07",
                      "CUSTID": "000074",
                      "CUSTOMERNAME": "MARIAMA S. CONTEH",
                      "MAKER": "TAKEON02",
                      "MAKERSTAMP": "2026-10-14 09:22:07",
                      "MODNO": 1,
                      "REFNO": "101BSTC262870002",
                      "RESTRTYPE": "DO",
                      "TXNSTAT": "O",
                      "accStatDetail": [
                        {
                          "ACC": "1010000740801013",
                          "ACCBRN": "101",
                          "ACCCURR": "SLE",
                          "ACCDESC": "MARIAMA S. CONTEH",
                          "ACCOUNTCLASS": "STSVEI"
                        }
                      ]
                    }
                  },
                  "FCUBSHEADER": {"MSGSTAT": "SUCCESS", "OPERATION": "CreateStatChange"}
                }
                """;
        WebClient webClient = WebClient.builder()
                .baseUrl("http://bst-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(rawResponse)
                        .build()))
                .build();

        StatusChangeResponse response = new AccountStatusService(webClient)
                .changeAccountStatus(new StatChangeRequest());

        assertEquals("SUCCESS", response.getFcubsheader().getMsgstat());
        assertEquals("000074", response.getFcubsbody().getAccStatMasterFull().getCustid());
        assertEquals("101BSTC262870002", response.getFcubsbody().getAccStatMasterFull().getRefno());
        assertEquals("1010000740801013", response.getFcubsbody().getAccStatMasterFull()
                .getAccStatDetail().get(0).get("ACC").asText());
        assertEquals("ST-SAVE-002", response.getFcubsbody().getFcubswarningresp().get(0)
                .getWarning().get(0).getWcode());
    }
}
