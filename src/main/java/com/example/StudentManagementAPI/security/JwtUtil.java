package com.example.StudentManagementAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

// Utility class responsible for creating and validating JWTs.
// This is the only class that knows about the secret signing key
@Component
public class JwtUtil {

    // Secret key used to sign and verify tokens.
    // In production, this MUST be loaded from an environment variable or
    // application.yml — never hardcoded in source control.
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "ThisIsASecretKeyThatShouldBeAtLeast256BitsLongForHS256".getBytes());

    // How long a token remains valid after issuance (in milliseconds).
    private final long expirationMs = 1000 * 60 * 60 ; // 1hr

    // Builds a signed JWT containing the username (as subject) and role (as a custom claim).
    public String generateToken(String userName, String userRole) {
        return Jwts.builder()
                .subject(userName)                    // "sub" claim — who this token belongs to
                .claim("userRole" , userRole)       // custom claim — used for role-based access
                .issuedAt(new Date())                 // "iat" claim — when the token was created
                .expiration(new Date(System.currentTimeMillis() + expirationMs))    // "exp" claim
                .signWith(secretKey)                   // cryptographically signs the token
                .compact();                             // serializes to the final header.payload.signature string
    }

    // Pulls the username back out of a token's payload.
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Pulls the role claim back out of a token's payload.
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Checks whether a token's expiration timestamp has already passed.
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Confirms a token belongs to the given username and hasn't expired.
    public boolean validateToken(String token, String username) {
        return extractUserName(token).equals(username) && !isTokenExpired(token);
    }

    // Shared helper: parses and verifies the token's signature, then applies
    // the given function to pull out whichever claim the caller needs.
    // If the signature is invalid or the token is malformed, this throws —
    // callers are expected to catch that (see JwtAuthFilter).
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)              // verifies the signature matches our secret key
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }
}
