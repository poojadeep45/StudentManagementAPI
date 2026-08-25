package com.example.StudentManagementAPI.config;

import com.example.StudentManagementAPI.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

// Central configuration for all HTTP-level security rules:
// which endpoints are public, which require login, and which require a specific role.

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    @Order(4)
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF protection is for cookie/session-based browser auth — irrelevant
                // for a stateless, token-based API, so we disable it.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Registration and login must always be reachable without a token —
                        // you can't log in if logging in itself requires being logged in.
                        .requestMatchers("/auth/**").permitAll()      // Anyone can read data — no login required for GET requests.
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // JWTs are stateless by design — the server keeps no memory of who's
                // logged in between requests. Every request re-proves identity via its token.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS))

                // Insert our custom JWT filter before Spring's default username/password filter,
                // so our token-based authentication runs first on every request.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
