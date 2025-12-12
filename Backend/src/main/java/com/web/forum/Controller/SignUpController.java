package com.web.forum.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.User;
import com.web.forum.Roles;
import com.web.forum.Service.JwtService;
import com.web.forum.Service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class SignUpController {

    //Initialize services
    private final JwtService jwtService;
    private final UserService userService;

    //Logger
    private final Logger log = LoggerFactory.getLogger(SignUpController.class);

    //Constructor
    public SignUpController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    //Register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, @RequestParam String confirmPassword) {

        //Compare password and confirmPassword values
        if (user.getPassword().equals(confirmPassword)) {
            //If matching => register user
            User newUser = userService.registerNewUser(user.getName(), user.getPassword(), Roles.USER);
            log.info("Created User: " + newUser.getName());
            return ResponseEntity.status(HttpStatus.OK).body("Created: " + newUser);
        } else {
            //If not matching => error
            log.info("Password and confirmedPassword dont't match");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and confirmedPassword dont't match");
        }
    }

    //Login as user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        //Expiry Interval = 1 Day
        int expiryInterval = 24 * 60 * 60;

        //If user is already logged in => error
        if (userService.isLoggedIn(request)) {
            log.info("Already logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User " + user.getName() + " is already logged in");
        }

        //If username and password are correct => generate jwt token
        String token = jwtService.generateToken(user.getName(), expiryInterval);

        // Send JWT token in an HTTP-Only cookie
        ResponseCookie cookie = ResponseCookie.
                from("jwtToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expiryInterval)
                .sameSite("none")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        log.info("Create Token for login: " + token);

        // Return success response
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user.getName() + " logged in");
    }

    //Logout user by deleting jwt token cookie
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Check for jwt token in cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    //Extract jwt token
                    String jwtToken = cookie.getValue();
                    //Validate jwt token and delete if validated
                    if (jwtToken != null || !jwtService.isTokenExpired(jwtToken)) {
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
