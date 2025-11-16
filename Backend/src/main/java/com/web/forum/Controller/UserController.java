package com.web.forum.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.User;
import com.web.forum.Roles;
import com.web.forum.Service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    //Add new article
    @PostMapping("/register")
    public User registerUser(@RequestBody String name, String password, Enum<Roles> roles) {
        return userService.registerNewUser(name, password, roles);
    }
}
