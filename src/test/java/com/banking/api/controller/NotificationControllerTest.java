package com.banking.api.controller;

import com.banking.api.config.SecurityConfig;
import com.banking.api.dto.EmailRequest;
import com.banking.api.dto.SmsBulkRequest;
import com.banking.api.dto.SmsRequest;
import com.banking.api.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@WebMvcTest(NotificationController.class)
@Import(SecurityConfig.class)
class NotificationControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private NotificationService service;
    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void requiresBothClientAndOperationRole() throws Exception {
        var client = new SimpleGrantedAuthority("ROLE_CLIENT");
        var email = new SimpleGrantedAuthority("ROLE_SEND_EMAIL");
        var sms = new SimpleGrantedAuthority("ROLE_SEND_SMS");
        String path = "/api/service/notifications/email";
        mvc.perform(post(path).contentType("application/json").content("{}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post(path).with(jwt().authorities(client)).contentType("application/json").content("{}"))
                .andExpect(status().isForbidden());
        mvc.perform(post(path).with(jwt().authorities(email)).contentType("application/json").content("{}"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/service/notifications/sms/bulk").with(jwt().authorities(client, email))
                        .contentType("application/json").content("{}"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(service);

        when(service.sendEmail(any())).thenReturn(ResponseEntity.ok("sent"));
        mvc.perform(post(path).with(jwt().authorities(client, email)).contentType("application/json")
                        .content("{\"recipient\":\"test@example.com\",\"subject\":\"hello\",\"msgBody\":\"body\"}"))
                .andExpect(status().isOk()).andExpect(content().string("sent"));
        verify(service).sendEmail(argThat((EmailRequest request) ->
                "test@example.com".equals(request.getRecipient())
                        && "hello".equals(request.getSubject()) && "body".equals(request.getMsgBody())));
        when(service.sendSms(any())).thenReturn(ResponseEntity.badRequest().body("bad SMS"));
        mvc.perform(post("/api/service/notifications/sms").with(jwt().authorities(client, sms))
                        .contentType("application/json")
                        .content("{\"to\":\"123\",\"content\":\"hello\",\"refrence\":\"abc\"}"))
                .andExpect(status().isBadRequest()).andExpect(content().string("bad SMS"));
        verify(service).sendSms(argThat((SmsRequest request) ->
                "123".equals(request.getTo()) && "abc".equals(request.getRefrence())
                        && "SkyAlert".equals(request.getFrom())));
        when(service.sendBulkSms(any())).thenReturn(ResponseEntity.ok("queued"));
        mvc.perform(post("/api/service/notifications/sms/bulk").with(jwt().authorities(client, sms))
                        .contentType("application/json")
                        .content("{\"content\":\"hello\",\"recipients\":[{\"to\":\"123\",\"reference\":\"ref\"}]}"))
                .andExpect(status().isOk()).andExpect(content().string("queued"));
        verify(service).sendBulkSms(argThat((SmsBulkRequest request) ->
                "SkyAlert".equals(request.getFrom()) && request.getRecipients().size() == 1
                        && "123".equals(request.getRecipients().get(0).getTo())
                        && "ref".equals(request.getRecipients().get(0).getReference())));
    }
}
