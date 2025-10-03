package com.standard.demo.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

public interface JwtService {
    String generateToken(String username, String password);

    Key getKey();

    String getUsername(String token);

    Boolean isTokenExpired();



    Boolean validateToken(String token, UserDetails userDetails);
}
