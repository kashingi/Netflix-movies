package com.netflix.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Add your annotations here
@Component
public class JwtUtil {

    private static final long JWT_TOKEN_VALIDITY = 30L * 24 * 60 * 60 * 1000;

    @Value("${jwt.secret:0eb58ff228c5706750152837c27d38b1d2905e8be2690e1058453cb00372d70e376686a70606c5a15afe19dc7eb2758579bc302f8ec6886206bdec17692a8828}")
    private String secret;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token, claims -> claims.get("role", String.class));
    }

    public Date getExpirationDateFromToken(String token) {

        return getClaimsFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return doGenerateToken(claims, username);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);

            return !isTokenExpired(token);
        }catch (Exception ex) {
            return false;
        }
    }
}
