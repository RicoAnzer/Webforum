package com.web.forum.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.Authentication.RegistrationRequest;
import com.web.forum.Security.Error.CustomErrors.BadRequestError;
import com.web.forum.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    // Register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        // Validating input
        String username = request.getUsername();
        String password = request.getPassword();
        String confirmedPassword = request.getConfirmedPassword();
        // Check validity of input
        if ("".equals(username)) {
            throw new BadRequestError("Username is empty");
        }
        if ("".equals(password)) {
            throw new BadRequestError("Please choose a password");
        }
        if (username.length() > 20) {
            throw new BadRequestError("Please choose an username with less than 20 characters");
        }
        if (!password.equals(confirmedPassword)) {
            throw new BadRequestError("Password and confirmedPassword don't match");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerNewUser(request));
    }

    // Login as user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginCredentials loginCredentials, HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.userLogin(loginCredentials, request, response));
    }

    // Logout user by deleting jwt token cookie
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.logoutUser(request, response));
    }
}
