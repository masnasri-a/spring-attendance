package com.alien.attendance.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class Security {

    private final String secretKey;

    public Security() {
        this.secretKey = "40ad554a6f2b3c981de12ace425ebf36cd1020ae0753b2cce83cec2e3ed6f444";
    }

    public String hashPassword(String password) {
        UUID uuid = UUID.nameUUIDFromBytes(password.getBytes());
        return uuid.toString();
    }

    public boolean checkPassword(String password, String hashedPassword) {
        UUID uuid = UUID.nameUUIDFromBytes(password.getBytes());
        return uuid.toString().equals(hashedPassword);
    }

    public String createToken(Long id, String email, String username) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(this.secretKey),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", id);
        claims.put("email", email);
        claims.put("name", username);
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(30L, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
    }

    public Jws<Claims> decodeToken(String token) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(this.secretKey),
                SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(token);
    }

    public Map<String, Object> payloadToken(String token) {
        String withoutBearer = token.replace("Bearer ", "");
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(this.secretKey),
                SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> claim = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(withoutBearer);
        Map<String, Object> body = claim.getBody();
        return body;
    }

    public ResponseEntity<?> validateToken(String token) {
        try {
            if (token.indexOf("Bearer ") == -1) {
                return ResponseEntity.status(401).body("Unauthorized");
            }
            String withoutBearer = token.replace("Bearer ", "");
            Jws<Claims> claims = decodeToken(withoutBearer);
            return ResponseEntity.ok().body(claims);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
    }
}
