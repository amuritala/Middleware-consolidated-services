package com.banking.api.service;

import com.banking.api.dto.AccountClass;
import com.banking.api.dto.CustomerCategory;
import com.banking.api.dto.Location;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountClassServiceTest {

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
