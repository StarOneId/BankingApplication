package com.bank.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bank.common.constants.AppConstants;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class JwtTokenProvider {

    public String generateToken(String username, List<String> roles) {
        return JWT.create()
            .withSubject(username)
            .withClaim("roles", roles)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + AppConstants.JWT_EXPIRATION))
            .sign(Algorithm.HMAC256(AppConstants.JWT_SECRET));
    }

    public String generateRefreshToken(String username) {
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + AppConstants.REFRESH_TOKEN_EXPIRATION))
            .sign(Algorithm.HMAC256(AppConstants.JWT_SECRET));
    }

    public String extractUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(AppConstants.JWT_SECRET))
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public List<String> extractRoles(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(AppConstants.JWT_SECRET))
                .build()
                .verify(token)
                .getClaim("roles")
                .asList(String.class);
        } catch (JWTVerificationException e) {
            return new ArrayList<>();
        }
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(AppConstants.JWT_SECRET))
                .build()
                .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
