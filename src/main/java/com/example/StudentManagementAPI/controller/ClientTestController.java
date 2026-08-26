package com.example.StudentManagementAPI.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth2 Client Test", description = "Diagnostic endpoint for verifying the OAuth2 client login flow")
@RestController
@RequestMapping("/client")
public class ClientTestController {

    @Operation(summary = "Show the logged-in OAuth2 user's subject ID", description = "Returns the 'sub' claim from the OAuth2 provider once login succeeds")
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        return "Login as : " + principal.getAttribute("sub");
    }
}
