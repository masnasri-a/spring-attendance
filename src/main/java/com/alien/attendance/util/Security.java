package com.alien.attendance.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class Security {

    public String hashPassword(String password)  {
        UUID uuid = UUID.nameUUIDFromBytes(password.getBytes());
        return uuid.toString();
    }

    public boolean checkPassword(String password, String hashedPassword) {
        UUID uuid = UUID.nameUUIDFromBytes(password.getBytes());
        return uuid.toString().equals(hashedPassword);
    }

    public String createToken(String email, String username) {
        String secretKey = "40ad554a6f2b3c981de12ace425ebf36cd1020ae0753b2cce83cec2e3ed6f444";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        Map<String, Object> claims = new HashMap<>();
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

    public static Jws<Claims> decodeToken(String token, String secret) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(token);
    }
}
