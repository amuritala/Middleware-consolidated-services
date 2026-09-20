package com.banking.api.service;

import com.banking.api.dto.AmtBlockNoRequest;
import com.banking.api.dto.CustomerCreateRequest;
import com.banking.api.dto.CustomerNumberRequest;
import com.banking.api.dto.CustomerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerServiceTest {

    @Test
    void acceptsDateOnlyCustomerPersonalDates() throws Exception {
        String json = """
                {
                  "custpersonal": {
                    "dob": "1983-10-05",
                    "pptissdt": "2026-09-19",
                    "pptexpdt": "2030-09-19"
                  }
                }
                """;

        CustomerCreateRequest request = new ObjectMapper()
                .findAndRegisterModules()
                .readValue(json, CustomerCreateRequest.class);

        assertEquals(1983, request.getCustpersonal().getDob().getYear());
        assertEquals(10, request.getCustpersonal().getDob().getMonth());
        assertEquals(5, request.getCustpersonal().getDob().getDay());
        assertEquals(2026, request.getCustpersonal().getPptissdt().getYear());
        assertEquals(2030, request.getCustpersonal().getPptexpdt().getYear());
    }

    @Test
    void acceptsCompleteCreateCustomerPayload() throws Exception {
        String json = """
                {
                  "addrln1": "string",
                  "country": "SL",
                  "ccateg": "INDV",
                  "fullname": "Nill Jalo Sese Lami",
                  "media": "MAIL",
                  "loc": "FTN",
                  "sname": "Jalo sese",
                  "nlty": "SL",
                  "uidname": "IDCARD",
                  "uidval": "09473837",
                  "createacc": "N",
                  "custpersonal": {
                    "fstname": "Nill",
                    "midname": "SESE",
                    "lstname": "JALO",
                    "dob": "1983-10-05",
                    "gendr": "M",
                    "nationid": "362728",
                    "telephno": "094827272",
                    "lang": "ENG",
                    "pptno": "1R7R7WW",
                    "pptissdt": "2026-09-19",
                    "pptexpdt": "2030-09-19"
                  }
                }
                """;

        CustomerCreateRequest request = new ObjectMapper()
                .findAndRegisterModules()
                .readValue(json, CustomerCreateRequest.class);

        assertEquals("Nill Jalo Sese Lami", request.getFullname());
        assertEquals("IDCARD", request.getUidname());
        assertEquals(1983, request.getCustpersonal().getDob().getYear());
        assertEquals(10, request.getCustpersonal().getDob().getMonth());
        assertEquals(5, request.getCustpersonal().getDob().getDay());
    }

    @Test
    void delegatesCustomerOperationsAndReturnsTypedBody() {
        List<String> paths = new ArrayList<>();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://customer-service")
                .exchangeFunction(request -> {
                    paths.add(request.url().getPath());
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body("{\"fcubsheader\":{\"msgstat\":\"SUCCESS\"},\"fcubsbody\":{}}")
                            .build());
                })
                .build();
        CustomerService service = new CustomerService(webClient);

        assertNotNull(service.createCustomer(new CustomerCreateRequest()).getFcubsbody());
        assertNotNull(service.queryCustomer(new CustomerNumberRequest()).getFcubsbody());
        assertNotNull(service.queryAmountBlock(new AmtBlockNoRequest()).getFcubsbody());

        assertEquals(List.of(
                "/api/v1/createcust",
                "/api/v1/QueryCustomer",
                "/api/v1/QueryAmtBlk"), paths);
    }

    @Test
    void mapsCustomerResponseWithFullFcubsHeaderAndPreservesMessageStatus() throws Exception {
        String rawResponse = """
                {
                  "fcubsheader": {
                    "source": "FCAT",
                    "ubscomp": "FCUBS",
                    "msgid": "6126263007436918",
                    "operation": "CreateCustomer",
                    "msgstat": "SUCCESS",
                    "destination": "FCAT"
                  },
                  "fcubsbody": {
                    "customerFull": {
                      "custno": "054853",
                      "fullname": "Waidi Adekunle Lami",
                      "custpersonal": {
                        "fstname": "Waidi"
                      }
                    },
                    "fcubserrorresp": [],
                    "fcubswarningresp": []
                  }
                }
                """;
        WebClient webClient = WebClient.builder()
                .baseUrl("http://customer-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(rawResponse)
                        .build()))
                .build();

        CustomerResponse response = new CustomerService(webClient)
                .createCustomer(new CustomerCreateRequest());

        assertEquals("SUCCESS", response.getFcubsheader().getMsgstat());
        assertEquals("054853", response.getFcubsbody().getCustomerFull().get("custno"));
        Map<?, ?> personal = (Map<?, ?>) response.getFcubsbody().getCustomerFull().get("custpersonal");
        assertEquals("Waidi", personal.get("fstname"));
    }
}
