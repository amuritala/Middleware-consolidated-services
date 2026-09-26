package com.banking.api.service;

import com.banking.api.dto.AccountClass;
import com.banking.api.dto.CustomerCategory;
import com.banking.api.dto.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class AccountClassService {
    public String serviceName = "Account Service";
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public AccountClassService(@Qualifier("accountClassServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Location> getAllLocations() {
        return getList("api/v1/accountclass/listlocations", Location.class, "locations");
    }

    public List<CustomerCategory> getAllCustomerCategories() {
        return getList("api/v1/accountclass/customercat", CustomerCategory.class, "customer categories");
    }

    public List<AccountClass> getAllAccountClasses() {
        return getList("api/v1/accountclass/accountClass", AccountClass.class, "account classes");
    }

    private <T> List<T> getList(String uri, Class<T> responseType, String operation) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for {}: {}", operation, rawResponse))
                .map(rawResponse -> readList(rawResponse, responseType, operation))
                .doOnError(ex -> log.error("Querying {} failed: {}", operation, ex.getMessage()))
                .block();
    }

    private <T> List<T> readList(String rawResponse, Class<T> responseType, String operation) {
        try {
            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, responseType);
            return objectMapper.readValue(rawResponse, listType);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for " + operation, exception);
        }
    }
}
