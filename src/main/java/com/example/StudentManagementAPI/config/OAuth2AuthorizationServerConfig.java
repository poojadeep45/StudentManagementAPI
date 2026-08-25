package com.example.StudentManagementAPI.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

// This entire class configures ONLY the OAuth2-specific endpoints
// (/oauth2/authorize, /oauth2/token, /oauth2/jwks, etc). It never touches
// or competes with the main JWT-based SecurityConfig chain, because
// the filter chain below is scoped with .securityMatcher(...) to just
// those endpoints — it does not match "any request".

@Configuration
public class OAuth2AuthorizationServerConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

        // Registers one "client application" allowed to request tokens from
        // this Authorization Server. In a real system this would live in a
        // database table; here it's a single in-memory entry for learning.
        @Bean
        public RegisteredClientRepository registeredClientRepository() {
            RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("student-api-client")
                    .clientSecret(passwordEncoder.encode("client-secret"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri("http://localhost:8080/login/oauth2/code/student-client")
                    .scope(OidcScopes.OPENID)
                    .scope("read")
                    .build();

            return new InMemoryRegisteredClientRepository(client);
        }

        // Security chain scoped ONLY to OAuth2's own endpoints. Adds a real
        // login page (formLogin) so a browser hitting /oauth2/authorize
        // unauthenticated gets a login form, not a raw 401 — this chain is
        // session-based, which is fine since it never overlaps with the
        // stateless JWT chain in SecurityConfig.
        @Bean
        @Order(1)
        public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
            OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                    OAuth2AuthorizationServerConfigurer.authorizationServer();

            RequestMatcher loginMatcher = request -> "/login".equals(request.getServletPath());
            RequestMatcher combinedMatcher = new OrRequestMatcher(
                    authorizationServerConfigurer.getEndpointsMatcher(),
                    loginMatcher
            );
            http.securityMatcher(combinedMatcher)
                    .with(authorizationServerConfigurer,
                            (authServer) -> authServer.oidc(Customizer.withDefaults()))
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .formLogin(Customizer.withDefaults());

            return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .securityMatcher("/api/oauth2/**")
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

            return http.build();

        }

    // The signing key pair for issued tokens. The Authorization Server signs
    // with the private key; any Resource Server verifies with the matching
    // public key (fetched via /oauth2/jwks) — asymmetric, since multiple
    // separate servers need to trust the token without sharing a secret.

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRSAKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey  privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);

    }

    public static KeyPair generateRSAKey() {
            try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
            }catch (NoSuchAlgorithmException ex){
                throw new IllegalStateException(ex);
            }
    }

    // Sets the issuer URL baked into every token issued. Resource Servers
    // check this to confirm a token actually came from this Authorization
    // Server, and it's what the discovery endpoint publishes.

    @Bean
    public AuthorizationServerSettings authoriztionServerSettings(){
            return AuthorizationServerSettings.builder()
                    .issuer("http://localhost:8080")
                    .build();
    }
}
