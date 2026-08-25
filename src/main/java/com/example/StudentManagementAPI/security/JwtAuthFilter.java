package com.example.StudentManagementAPI.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Runs once per incoming HTTP request, before the request reaches any controller.
// Its job: look for a JWT in the Authorization header, and if it's valid,
// tell Spring Security who's making this request (so @PreAuthorize / hasRole checks work).
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Only attempt authentication if a Bearer token was actually provided.
        // If missing, we just let the request continue unauthenticated —
        // SecurityConfig decides downstream whether that endpoint requires auth.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // strip the "Bearer " prefix

            try {
                String username = jwtUtil.extractUserName(token);
                String role = jwtUtil.extractUserRole(token);

                if (username != null && !jwtUtil.isTokenExpired(token)) {
                    // Build a Spring Security "Authentication" object representing this user.
                    // Roles must be prefixed with "ROLE_" for hasRole()/hasAuthority() to match correctly.
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username, null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            );
                    // Register this authentication for the current request's lifetime.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // Token was malformed, tampered with, or expired.
                // We deliberately swallow the exception here and leave the request
                // unauthenticated — SecurityConfig will reject it downstream with 401/403
                // if the endpoint requires authentication.
            }
        }

        // Always continue the filter chain, whether or not authentication succeeded.
        filterChain.doFilter(request, response);
    }
}