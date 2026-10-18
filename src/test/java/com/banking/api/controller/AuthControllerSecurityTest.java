package com.banking.api.controller;

import com.banking.api.config.SecurityConfig;
import com.banking.api.dto.AuthResponse;
import com.banking.api.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void allowsAnonymousLoginRequestThroughSecurity() throws Exception {
        when(authService.login(any()))
                .thenReturn(new AuthResponse("access-token", "refresh-token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer stale-or-invalid-token")
                        .content("""
                                {"username":"customer","password":"secret"}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "access_token": "access-token",
                          "refresh_token": "refresh-token"
                        }
                        """));

        verify(authService).login(any());
    }

    @Test
    void allowsAnonymousRefreshRequestThroughSecurity() throws Exception {
        when(authService.refresh(any()))
                .thenReturn(new AuthResponse("new-access", "new-refresh"));

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refresh_token":"old-refresh"}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"access_token": "new-access", "refresh_token": "new-refresh"}
                        """));

        verify(authService).refresh(any());
    }

    @Test
    void allowsAnonymousLogoutRequestThroughSecurity() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refresh_token":"old-refresh"}
                                """))
                .andExpect(status().isNoContent());

        verify(authService).logout(any());
    }
}
