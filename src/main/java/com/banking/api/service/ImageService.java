package com.banking.api.service;

import com.banking.api.dto.ImageSignatureRequest;
import com.banking.api.dto.ImageSignatureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class ImageService {

    private final WebClient webClient;

    public ImageService(@Qualifier("imageServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ImageSignatureResponse queryImage(ImageSignatureRequest request) {
        return webClient.post()
                .uri("api/v1/QueryImage")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ImageSignatureResponse.class)
                .doOnError(ex -> log.error("Querying image signature failed: {}", ex.getMessage()))
                .block();
    }
}
