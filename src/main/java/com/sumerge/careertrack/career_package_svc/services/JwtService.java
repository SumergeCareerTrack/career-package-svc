package com.sumerge.careertrack.career_package_svc.services;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sumerge.careertrack.career_package_svc.repositories.UserTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${redis.secretkey}")
    private String secretKey;

    private final UserTokenRepository userTokenRepository;

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
        final String email = extractEmail(token);

        boolean userHasToken = userTokenRepository.existsByEmail(email);

        return userHasToken && !isTokenExpired(token);
    }

    boolean isTokenExpired(String token) throws ExpiredJwtException {
        Date expiration = extractClaim(token, Claims::getExpiration);
        if (!expiration.before(new Date())) {
            return false;
        } else {
            throw new ExpiredJwtException(null, null, "Token has expired");
        }
    }

    Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
