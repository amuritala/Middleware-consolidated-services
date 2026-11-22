package com.banking.api.service;

import com.banking.api.dto.AccountClass;
import com.banking.api.dto.CustomerCategory;
import com.banking.api.dto.Location;
import com.banking.api.dto.NinRequest;
import com.banking.api.dto.UpdateNinRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountClassServiceTest {

    private AccountClassService serviceReturning(String body) {
        return new AccountClassService(WebClient.builder()
                .baseUrl("http://account-class-service")
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(body)
                        .build()))
                .build());
    }

    @Test
    void mapsNinLookupArraysAndLegacyObject() {
        assertEquals(0, serviceReturning("[]").nin(new NinRequest("missing")).size());
        var matches = serviceReturning("""
                [{"customer_no":"123","unique_id_value":"a"},
                 {"customer_no":"456","unique_id_value":"b"}]
                """).nin(new NinRequest("a"));
        assertEquals(2, matches.size());
        assertEquals("456", matches.get(1).getCustomer_no());
        assertEquals("123", serviceReturning("{\"customer_no\":\"123\"}")
                .nin(new NinRequest("a")).get(0).getCustomer_no());
        assertThrows(IllegalStateException.class,
                () -> serviceReturning("\"not an object\"").nin(new NinRequest("a")));
    }

    @Test
    void mapsPlainTextAndJsonNinUpdateResponses() {
        var request = new UpdateNinRequest("123", "id");
        assertEquals("Nin status updated successfully", serviceReturning("Nin status updated successfully")
                .updatenin(request).getMessage());
        assertEquals("Updated", serviceReturning("{\"message\":\"Updated\"}")
                .updatenin(request).getMessage());
        assertEquals("Updated", serviceReturning("\"Updated\"").updatenin(request).getMessage());
        assertThrows(IllegalStateException.class,
                () -> serviceReturning("{invalid").updatenin(request));
    }

    @Test
    void queriesTypedAccountClassLookups() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://account-class-service")
                .exchangeFunction(request -> {
                    String body = switch (request.url().getPath()) {
                        case "/api/v1/accountclass/listlocations" ->
                                "[{\"loc_code\":\"001\",\"description\":\"Head Office\"}]";
                        case "/api/v1/accountclass/customercat" ->
                                "[{\"cust_cat\":\"RET\",\"cust_cat_desc\":\"Retail\"}]";
                        case "/api/v1/accountclass/accountClass" ->
                                "[{\"account_class\":\"SAV\",\"description\":\"Savings\"}]";
                        default -> "[]";
                    };
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body(body)
                            .build());
                })
                .build();

        var service = new AccountClassService(webClient);

        Location location = service.getAllLocations().get(0);
        CustomerCategory category = service.getAllCustomerCategories().get(0);
        AccountClass accountClass = service.getAllAccountClasses().get(0);

        assertEquals("001", location.getLocCode());
        assertEquals("RET", category.getCustCat());
        assertEquals("SAV", accountClass.getAccountClass());
    }
}
