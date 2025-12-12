package com.web.forum.Security;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

//Security Config filter
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //JwtAuthFilter
    private final JwtAuthFilter jwtAuthFilter;

    //Constructor
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    //Assign JwtAuthFilter and CORS, manage React -> Spring Boot CORS and request authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                // Allow everyone access to registration and login => for logout authorization is necessary
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                //Must be logged in for everything else
                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Add custom filter (JwtAuthFilter)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                //Assign cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return httpSecurity.build();
    }

    //Cors configuration
    private CorsConfigurationSource corsConfigurationSource() {
        return (HttpServletRequest request) -> {
            CorsConfiguration ccfg = new CorsConfiguration();
            //Allow access to React/vite port
            ccfg.setAllowedOrigins(Arrays.asList("https://localhost:5173"));
            ccfg.setAllowedMethods(List.of("*"));
            ccfg.setAllowCredentials(true);
            ccfg.setAllowedHeaders(List.of("*"));
            ccfg.setMaxAge(3600L);
            return ccfg;
        };
    }
}
