package com.example.StudentManagementAPI.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "OAuth2 Resource Server Test", description = "Diagnostic endpoint for verifying JWT-based resource server authentication")
@RestController
@RequestMapping("/api/oauth2")
public class ResourceServerTestController {

    @Operation(summary = "Show the authenticated JWT's claims", description = "Returns the subject, scope, and issuer from the validated access token")
    @GetMapping("/profile")
    public Map<String, Object> profile(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "subject" , jwt.getSubject(),
                "scopes", jwt.getClaimAsString("scope"),
                "issuer", jwt.getIssuer().toString()
        );
    }
}