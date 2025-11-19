package com.web.forum.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.User;
import com.web.forum.Service.UserService;

@RestController
@RequestMapping("/auth")
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService)
    {
        this.userService = userService;
    }

    //Register user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerNewUser(user.getName(), user.getPassword(), user.getRole());
    }
    
    //Add new article
    @GetMapping("/index")
    public String Index(){
        return "index";
    }    
    
}
