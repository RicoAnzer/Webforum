package com.web.forum.Service;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.forum.Repository.UserRepository;
import com.web.forum.Security.JwtConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

//All methods for jwt token handling
@Service
public class JwtService {

    @Autowired
    private UserRepository userRepository;

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

    //Check if user is logged in
    public Boolean isLoggedIn(HttpServletRequest request) {
        String jwtToken = null;
        // Check for jwt token in cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    //Extract jwt token
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }
        // If token is not missing and not expired, return true
        return !(jwtToken == null
                || isTokenExpired(jwtToken)
                && userRepository.findByName(getUserName(jwtToken)) != null);
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
}
