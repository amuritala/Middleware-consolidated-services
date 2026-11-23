package com.banking.api.controller;

import com.banking.api.config.SecurityConfig;
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
        mvc.perform(post(path).with(jwt().authorities(client, email)).contentType("application/json").content("{}"))
                .andExpect(status().isOk()).andExpect(content().string("sent"));
        when(service.sendSms(any())).thenReturn(ResponseEntity.badRequest().body("bad SMS"));
        mvc.perform(post("/api/service/notifications/sms").with(jwt().authorities(client, sms))
                        .contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest()).andExpect(content().string("bad SMS"));
    }
}
