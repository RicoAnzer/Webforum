package com.web.forum.Service;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.web.forum.Security.JwtConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//All methods for jwt token handling
@Service
public class JwtService {

    //Generate SecretKey for jwt Token
    private final static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    //Generate jwt Token
    public String generateToken(String username, long expireInterval) {
        //Create token
        return Jwts
                .builder()
                //Starting date of token is current time
                .issuedAt(new Date(System.currentTimeMillis()))
                //Assign expiration date => Starting date + given expireInterval
                .expiration(new Date(System.currentTimeMillis() + expireInterval))
                //Add claims
                .subject(username)
                //Sign with SecretKey generated above
                .signWith(key)
                .compact();
    }

    //Extract username out of jwt token
    public String getUserName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    //Check if token is expired
    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        //Check current date => If current date is after expiration isTokenExpired() = true
        Date now = Date.from(Instant.now());
        return claims.getExpiration().before(now);
    }

    //Extract all claims out of jwt token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Check if token is valid => correct username and token not expired
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
