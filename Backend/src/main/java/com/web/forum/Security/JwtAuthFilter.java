package com.web.forum.Security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.web.forum.Service.JwtService;
import com.web.forum.Service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Custom OncePerRequestFilter to check if user is logged in
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    //Logger
    private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    //Filter process at the beginning of each request
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            processToken(request);

        } catch (Exception e) {
            log.error("Failed to process JWT Token: " + e.getMessage());
            // Pass exceptions to response
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        // Pass the control back to framework
        filterChain.doFilter(request, response);
    }

    //Check validity of token
    private void processToken(HttpServletRequest request) {
        String jwtToken = null;

        // Check if logged in
        if (!jwtService.isLoggedIn(request)) {
            return;
            //If logged in => extract jwt token
        } else {
            //Search cookies for jwt token
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwtToken".equals(cookie.getName())) {
                        //Extract jwt token
                        jwtToken = cookie.getValue();
                    }
                }
            }
        }

        //Check validity of token
        if (jwtService.isTokenExpired(jwtToken)) {
            logger.info("Token validity expired");
            return;
        }

        String userName = jwtService.getUserName(jwtToken);
        //Check username of token
        if (userName == null) {
            logger.info("No username found in JWT Token");
            return;
        }

        // Authenticate and create authentication instance
        UserDetails userDetails = userService.loadUserByUsername(userName);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Store authentication token for application to use
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
