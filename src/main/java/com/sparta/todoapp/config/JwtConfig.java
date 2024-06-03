package com.sparta.todoapp.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;

@Configuration
public class JwtConfig {
    @Value("${jwt.access.secret.key}")
    private String accessSecretKey;

    @Value("${jwt.refresh.secret.key}")
    private String refreshSecretKey;

    @Bean
    public Key accessKey() {
        byte[] accessBytes = Base64.getDecoder().decode(accessSecretKey);
        return Keys.hmacShaKeyFor(accessBytes);
    }

    @Bean
    public Key refreshKey() {
        byte[] refreshBytes = Base64.getDecoder().decode(refreshSecretKey);
        return Keys.hmacShaKeyFor(refreshBytes);
    }

    @Bean
    public SignatureAlgorithm signatureAlgorithm() {
        return SignatureAlgorithm.HS256;
    }
}
