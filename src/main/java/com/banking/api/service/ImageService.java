package com.banking.api.service;

import com.banking.api.dto.ImageSignatureRequest;
import com.banking.api.dto.ImageSignatureResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class ImageService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public ImageService(@Qualifier("imageServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ImageSignatureResponse queryImage(ImageSignatureRequest request) {
        return webClient.post()
                .uri("api/v1/QueryImage")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> log.info("Raw response for querying image signature: {}", rawResponse))
                .map(this::readResponse)
                .doOnError(ex -> log.error("Querying image signature failed: {}", ex.getMessage()))
                .block();
    }

    private ImageSignatureResponse readResponse(String rawResponse) {
        try {
            return objectMapper.readValue(rawResponse, ImageSignatureResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to deserialize raw response for querying image signature",
                    exception);
        }
    }
}
