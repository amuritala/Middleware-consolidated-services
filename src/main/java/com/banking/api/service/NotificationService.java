package com.banking.api.service;

import com.banking.api.dto.EmailRequest;
import com.banking.api.dto.SmsBulkRequest;
import com.banking.api.dto.SmsRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
public class NotificationService {
    private final WebClient webClient;

    public NotificationService(@Qualifier("notificationServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseEntity<String> sendEmail(EmailRequest request) {
        return post("/sendMail", request);
    }

    public ResponseEntity<String> sendSms(SmsRequest request) {
        return post("/api/vi/sendsms", request);
    }

    public ResponseEntity<String> sendBulkSms(SmsBulkRequest request) {
        return post("/api/vi/sendbulksms", request);
    }

    private ResponseEntity<String> post(String path, Object request) {
        return webClient.post()
                .uri(path)
                .bodyValue(request)
                .exchangeToMono(response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> {
                            HttpHeaders headers = new HttpHeaders();
                            response.headers().contentType().ifPresent(headers::setContentType);
                            return new ResponseEntity<>(body, headers, response.statusCode());
                        }))
                .timeout(Duration.ofSeconds(60))
                .onErrorMap(WebClientRequestException.class,
                        ex -> new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                                "Email SMS service is unavailable", ex))
                .onErrorMap(TimeoutException.class,
                        ex -> new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT,
                                "Email SMS service timed out", ex))
                .block();
    }
}
