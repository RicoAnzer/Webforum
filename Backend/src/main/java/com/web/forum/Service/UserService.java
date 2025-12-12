package com.web.forum.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.forum.Entity.User;
import com.web.forum.Repository.UserRepository;
import com.web.forum.Roles;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

//UserService to manage Users
@Service
public class UserService implements UserDetailsService {

    //UserRepository
    private final UserRepository userRepository;
    //SecurityConfig
    //private final SecurityConfig securityConfig;
    //JwtService
    private final JwtService jwtService;

    //Constructor
    public UserService(UserRepository userRepository, JwtService jwtService)
    {
        this.userRepository = userRepository;
        //this.securityConfig = securityConfig;
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Register a new User
    public User registerNewUser(String name, String password, Roles roles)
    {
        String encodedPassword = passwordEncoder().encode(password);
        //Format createdAt to "dd-MM-YYYY"
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        //Create User Object
        User newUser = new User(
            name, 
            encodedPassword, 
            roles,
            "",
            LocalDate.now().format(dateTimeFormatter),
            "",
            false
        );
        //Save newUser to database
        userRepository.save(newUser);
        //Return created User
        return newUser;
    }

    //User login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Find User by username
        User user = userRepository.findByName(username);
        //User == null => No User with this username, throw error
        if(user==null) {
            throw new UsernameNotFoundException("User not found with this name: "+username);
        }

        //List all authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        //Return UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                authorities);

    }

    //Check if user is logged in
    public Boolean isLoggedIn(HttpServletRequest request)
    {
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
        return !(jwtToken == null || jwtService.isTokenExpired(jwtToken));
    }
}
