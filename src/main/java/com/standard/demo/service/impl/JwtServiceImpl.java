package com.standard.demo.service.impl;

import com.standard.demo.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey ;

    @Value("${jwt.algorithm}")
    private String algorithm;

    @Value("${jwt.duration}")
    private Long duration;

    @Value("${jwt.zone}")
    private String zone;


    @Override
    public String generateToken(String username, String password) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("password",password);

        ZoneId zoneId = ZoneId.of(zone);

        Instant now = Instant.now(); // UTC
        Instant expirationInstant = now.plusSeconds(duration * 60);

        Date issuedAt = Date.from(now);
        Date expiration = Date.from(expirationInstant);

        // Debug: mostrar iat y exp en UTC y en la zona local
        ZonedDateTime issuedAtUTC = issuedAt.toInstant().atZone(ZoneId.of("UTC"));
        ZonedDateTime expirationUTC = expiration.toInstant().atZone(ZoneId.of("UTC"));

        ZonedDateTime issuedAtLocal = issuedAt.toInstant().atZone(zoneId);
        ZonedDateTime expirationLocal = expiration.toInstant().atZone(zoneId);

        System.out.println("Issued At UTC: " + issuedAtUTC);
        System.out.println("Expiration UTC: " + expirationUTC);
        System.out.println("Issued At " + zone + ": " + issuedAtLocal);
        System.out.println("Expiration " + zone + ": " + expirationLocal);
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .and()
                .signWith(getKey())
                .compact();
    }


    @Override
    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String getUsername(String token) {

        return extractUsername(token);
    }

    @Override
    public Boolean isTokenExpired() {
        return null;
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private <T> T extractClaim (String token, Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
