package com.banking.api.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class WebConfig {

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    @Value("${account-stats.service.url}")
    private String accountStatsServiceUrl;

    @Value("${account-fin.service.url}")
    private String accountFinServiceUrl;

    @Value("${de.service.url}")
    private String deServiceUrl;

    @Value("${bst.service.url}")
    private String bstServiceUrl;

    @Value("${image.service.url}")
    private String imageServiceUrl;

    @Value("${account-class.service.url}")
    private String accountClassServiceUrl;

    @Value("${rteller.service.url}")
    private String rTellerServiceUrl;

    @Value("${customer.service.max-buffer-size}")
    private Integer maxBufferSize;

    private String mediaType = MediaType.APPLICATION_JSON_VALUE;

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    @Bean
    WebClient customerWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "CustomerService")
                .baseUrl(customerServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient accountServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "AccountService")
                .baseUrl(accountServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient accountStatsServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "AccountStatsService")
                .baseUrl(accountStatsServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient accountFinServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "AccountFinancialService")
                .baseUrl(accountFinServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient deServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "DeService")
                .baseUrl(deServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient bstServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "AccountStatusService")
                .baseUrl(bstServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient imageServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "ImageService")
                .baseUrl(imageServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient accountClassServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "AccountClassService")
                .baseUrl(accountClassServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    WebClient rtellerServiceWebClient(WebClient.Builder webClientBuilder) {
        return configuredBuilder(webClientBuilder, "RtellerService")
                .baseUrl(rTellerServiceUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024))
                        .build())
                .defaultHeader("Accept", mediaType)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private WebClient.Builder configuredBuilder(WebClient.Builder builder, String serviceName) {
        return builder.clone().filter(downstreamAuditFilter(serviceName));
    }

    private ExchangeFilterFunction downstreamAuditFilter(String serviceName) {
        return (request, next) -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String path = request.url().getPath();
                String operation = path.replaceFirst("^/+", "").replaceFirst("^api/v1/", "");
                attributes.getRequest().setAttribute("downstream.serviceName", serviceName);
                attributes.getRequest().setAttribute("downstream.operation", operation);
            }
            return next.exchange(request);
        };
    }
}
