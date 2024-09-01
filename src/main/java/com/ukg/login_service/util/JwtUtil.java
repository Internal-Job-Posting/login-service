package com.ukg.login_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("classpath:private_key.pem")
    private Resource privateKeyResource;

    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey();
            System.out.println("Private Key successfully loaded");
        } catch (Exception e) {
            System.err.println("Error loading private key: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize JwtUtil", e);
        }
    }

    private PrivateKey loadPrivateKey() throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(privateKeyResource.getInputStream()))) {
            String privateKeyPEM = reader.lines()
                .filter(line -> !line.startsWith("-----BEGIN") && !line.startsWith("-----END"))
                .collect(Collectors.joining());

            byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
    }

    public String generateToken(String username, List<String> authorities) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}