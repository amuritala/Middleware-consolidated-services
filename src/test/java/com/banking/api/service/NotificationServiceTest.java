package com.banking.api.service;

import com.banking.api.dto.EmailRequest;
import com.banking.api.dto.Recipients;
import com.banking.api.dto.SmsBulkRequest;
import com.banking.api.dto.SmsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationServiceTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private HttpServer server;
    private NotificationService service;
    private final AtomicReference<String> path = new AtomicReference<>();
    private final AtomicReference<String> body = new AtomicReference<>();

    @BeforeEach
    void startServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            path.set(exchange.getRequestURI().getPath());
            body.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            boolean email = path.get().equals("/sendMail");
            boolean bulk = path.get().endsWith("sendbulksms");
            byte[] response = (email ? "Mail Sent Successfully"
                    : bulk ? "{\"error\":\"invalid recipients\"}" : "{\"status\":\"queued\"}")
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", email ? "text/plain" : "application/json");
            exchange.sendResponseHeaders(bulk ? 400 : 200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        server.start();
        service = new NotificationService(WebClient.create("http://127.0.0.1:" + server.getAddress().getPort()));
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    @Test
    void forwardsPayloadsAndPreservesDownstreamResponses() throws Exception {
        EmailRequest email = new EmailRequest("test@example.com", "body", "hello", "file");
        var emailResponse = service.sendEmail(email);
        assertEquals("/sendMail", path.get());
        assertEquals(mapper.readTree("""
                {"recipient":"test@example.com","subject":"hello","msgBody":"body","attachment":"file"}
                """), mapper.readTree(body.get()));
        assertEquals("Mail Sent Successfully", emailResponse.getBody());
        assertEquals(MediaType.TEXT_PLAIN, emailResponse.getHeaders().getContentType());

        SmsRequest sms = new SmsRequest("SkyAlert", "123", "hello", "abc", "https://example.com/callback");
        var smsResponse = service.sendSms(sms);
        assertEquals("/api/vi/sendsms", path.get());
        assertEquals(mapper.readTree("""
                {"from":"SkyAlert","to":"123","content":"hello","refrence":"abc",
                 "callbackUrl":"https://example.com/callback"}
                """), mapper.readTree(body.get()));
        assertEquals("{\"status\":\"queued\"}", smsResponse.getBody());

        SmsBulkRequest bulk = new SmsBulkRequest("SkyAlert", "hello", "https://example.com/callback",
                List.of(new Recipients("123", "abc"), new Recipients("456", "def")));
        var bulkResponse = service.sendBulkSms(bulk);
        assertEquals("/api/vi/sendbulksms", path.get());
        assertEquals(mapper.readTree("""
                {"from":"SkyAlert","content":"hello","callbackUrl":"https://example.com/callback",
                 "recipients":[{"to":"123","reference":"abc"},{"to":"456","reference":"def"}]}
                """), mapper.readTree(body.get()));
        assertEquals(HttpStatus.BAD_REQUEST, bulkResponse.getStatusCode());
        assertEquals("{\"error\":\"invalid recipients\"}", bulkResponse.getBody());
    }

    @Test
    void reportsUnavailableDownstream() {
        server.stop(0);
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.sendEmail(new EmailRequest("test@example.com", "body", "hello", null)));
        assertEquals(HttpStatus.BAD_GATEWAY, error.getStatusCode());
    }
}
