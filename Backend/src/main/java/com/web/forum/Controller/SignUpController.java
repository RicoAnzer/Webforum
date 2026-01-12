package com.web.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.Authentication.RegistrationRequest;
import com.web.forum.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class SignUpController {

    @Autowired
    private UserService userService;

    //Register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        return userService.registerNewUser(request);
    }

    //Login as user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginCredentials loginCredentials, HttpServletRequest request, HttpServletResponse response) {
        return userService.userLogin(loginCredentials, request, response);
    }

    //Logout user by deleting jwt token cookie
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        return userService.logoutUser(request, response);
    }
}
