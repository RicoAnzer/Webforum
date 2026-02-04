package com.web.forum.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.Authentication.RegistrationRequest;
import com.web.forum.Entity.Role;
import com.web.forum.Repository.RoleRepository;
import com.web.forum.Repository.UserRepository;
import com.web.forum.Repository.UserRoleRepository;
import com.web.forum.Security.JwtAuthFilter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//UserService to manage Authentication of users
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtService jwtService;

    //Logger
    private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //User login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Find User by username
        LoginCredentials credentials = userRepository.findCredByName(username);
        //LoginCredentials == null => No User with this username, throw error
        if (credentials == null) {
            throw new UsernameNotFoundException("User not found with this name: " + username);
        }
        //List all roles
        List<String> roles = userRoleRepository.findById(credentials.getUserId());

        //Create userdetails
        UserDetails user = User
                .withUsername(credentials.getUsername())
                .password(credentials.getPassword())
                .roles(roles.toArray(String[]::new))
                .build();
        //Return UserDetails
        return user;
    }

    //Register new user
    public ResponseEntity<?> registerNewUser(RegistrationRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String confirmedPassword = request.getConfirmedPassword();

        //If username is empty => error
        if ("".equals(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is empty");
        }
        //If username is too long => error
        if (username.length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please choose an username with less than 20 characters");
        }
        //Compare password and confirmPassword values
        if (password.equals(confirmedPassword)) {
            //If username is empty => error
            if ("".equals(password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please choose a password");
            }

            //Check if username already exists (if it doesn't exist: findByName() == null)
            if (userRepository.findByName(username) == null) {
                //Encode password
                String encodedPassword = passwordEncoder().encode(password);
                //Format createdAt to "dd-MM-YYYY"
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                //Id null to auto generate id at database
                Long id = null;

                //Empty roles List as placeholder 
                //=> Roles List shows all roles assigned to user for later use in Frontend 
                //(E.g. Only Moderators can add Topics, so Button to add them is invisible for common users)
                //=> User and Roles getting assigned in later step in table user_roles
                List<String> roles = new ArrayList<>();

                //Create User Object
                com.web.forum.Entity.User newUser = new com.web.forum.Entity.User(
                        id,
                        username,
                        roles,
                        "",
                        LocalDate.now().format(dateTimeFormatter),
                        "",
                        false
                );

                //Save newUser to database and autogenerate ID
                LoginCredentials credentials = userRepository.save(newUser, encodedPassword);
                //Bind Role 'USER' to created user in table user_roles
                try {
                    //Extract Role "USER"
                    //=> 1 = USER
                    if (credentials != null) {
                        Role role = roleRepository.find(1);
                        //Bind Role "USER" to user
                        userRoleRepository.save(credentials.getUserId(), role.getId());
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Can't bind User to Role");
                }

                return ResponseEntity.status(HttpStatus.CREATED).body(credentials);

            } else {
                //If name already in database => error
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }

        } else {
            //If not matching => error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and confirmedPassword don't match");
        }
    }

    //Login as user
    public ResponseEntity<?> userLogin(@RequestBody LoginCredentials loginCredentials, HttpServletRequest request, HttpServletResponse response) {
        //Expiry interval for cookie = 1 Day in Milliseconds
        int expiryInterval = 24 * 60 * 60 * 1000;
        //Load user Object from database
        com.web.forum.Entity.User loggedInUser = userRepository.findByName(loginCredentials.getUsername());

        //If user doesn't exist in database => error
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        //If user is already logged in => error
        if (jwtService.isLoggedIn(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already logged in");
        }
        //If password is wrong => error
        String encryptedPassword = userRepository.findCredByName(loginCredentials.getUsername()).getPassword();
        if (!passwordEncoder().matches(loginCredentials.getPassword(), encryptedPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password not found");
        }

        //If username and password are correct => generate jwt token...
        String token = jwtService.generateToken(loginCredentials.getUsername(), expiryInterval);
        log.info("Generate token: " + token);
        //Send JWT token in an HTTP-Only cookie
        ResponseCookie cookie = ResponseCookie.
                from("jwtToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expiryInterval)
                .sameSite("none")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // Return success response and user Object for frontend processing
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(loggedInUser);
    }

    //Logout user by deleting jwt token
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Check for jwt token in cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    //Extract jwt token
                    String jwtToken = cookie.getValue();
                    //Validate jwt token and delete if validated
                    if (jwtToken != null
                            || !jwtService.isTokenExpired(jwtToken)
                            && userRepository.findByName(jwtService.getUserName(jwtToken)) != null) {
                        //Create new cookie with same name and maxAge to 0 => delete Cookie
                        ResponseCookie newCookie = ResponseCookie.from("jwtToken", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("none")
                                .build();
                        response.addHeader("Set-Cookie", newCookie.toString());
                        log.info("Deleted token: " + jwtToken);
                        return ResponseEntity.status(HttpStatus.OK).body("Logged out succesfully");
                    }
                }
            }
        }
        //No token found => Not logged in
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
    }
}
