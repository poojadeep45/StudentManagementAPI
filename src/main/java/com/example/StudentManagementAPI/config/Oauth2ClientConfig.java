package com.example.StudentManagementAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@Profile("!railway")
public class Oauth2ClientConfig {
    @Bean
    @Order(3)
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher clientMatcher = request -> {
            String path = request.getServletPath();
            return path.startsWith("/client/")
                    || path.startsWith("/oauth2/authorization/")
                    || path.startsWith("/login/oauth2/code/");
        };
        http
                .securityMatcher(clientMatcher)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/client/home", true));

        return http.build();
    }
}
