package com.banking.api.service;

import com.banking.api.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
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


    public NinResponse nin(NinRequest request) {
        log.info("Getting Nin for customer number {}", request.getNin());
        return post("api/v1/accountclass/check_nin", request, "getting nin");
    }

    public NinUpdateResponse updatenin(UpdateNinRequest request) {
        log.info("Updating Nin for customer number {}", request.getCustomer_no());
        return postU("api/v1/accountclass/updateNin", request, "updating nin");
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

    private NinResponse post(String uri, Object request, String operation) {
        return webClient.post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for {}: {}", operation, rawResponse))
                .map(rawResponse -> readResponse(rawResponse, operation))
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }

    private NinResponse readResponse(String rawResponse, String operation) {
        try {
            JsonNode response = objectMapper.readTree(rawResponse);
            if (response == null || (!response.isArray() && !response.isObject())) {
                throw new IllegalStateException("Unexpected response shape for " + operation);
            }
            if (response.isArray()) {
                JavaType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, NinResponse.class);
                return objectMapper.readValue(rawResponse, listType);
            }
           // return List.of(objectMapper.treeToValue(response, NinResponse.class));
            return objectMapper.treeToValue(response, NinResponse.class);

        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for " + operation, exception);
        }
    }

    private NinUpdateResponse postU(String uri, Object request, String operation) {
        return webClient.post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for {}: {}", operation, rawResponse))
                .map(rawResponse -> readResponseU(rawResponse, operation))
                .doOnError(ex -> log.error("{} failed: {}", operation, ex.getMessage()))
                .block();
    }

    private NinUpdateResponse readResponseU(String rawResponse, String operation) {
        try {
            String trimmed = rawResponse.trim();
            if (!trimmed.isEmpty() && trimmed.charAt(0) != '{'
                    && trimmed.charAt(0) != '[' && trimmed.charAt(0) != '"') {
                return new NinUpdateResponse(rawResponse);
            }
            if (trimmed.startsWith("\"")) {
                return new NinUpdateResponse(objectMapper.readValue(rawResponse, String.class));
            }
            return objectMapper.readValue(rawResponse, NinUpdateResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for " + operation, exception);
        }
    }



}
