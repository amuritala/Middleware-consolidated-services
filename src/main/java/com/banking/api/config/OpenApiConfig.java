package com.banking.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.Scopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        String keycloakUrl = "http://localhost:8080";
        String realm = "banking";

        String authorizationUrl =
                keycloakUrl +
                        "/realms/" +
                        realm +
                        "/protocol/openid-connect/auth";

        String tokenUrl =
                keycloakUrl +
                        "/realms/" +
                        realm +
                        "/protocol/openid-connect/token";

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Banking API")
                                .version("1.0.0")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "keycloak",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OAUTH2)
                                                .flows(
                                                        new OAuthFlows()
                                                                .authorizationCode(
                                                                        new OAuthFlow()
                                                                                .authorizationUrl(authorizationUrl)
                                                                                .tokenUrl(tokenUrl)
                                                                                .scopes(
                                                                                        new Scopes()
                                                                                                .addString("openid", "OpenID")
                                                                                                .addString("profile", "Profile")
                                                                                )
                                                                )
                                                )
                                )
                );
    }
}



/**
 *
 Client type: OpenID Connect
 Client authentication: Off
 Authorization: Off
 Standard flow: On
 Direct access grants: Off
 * **/