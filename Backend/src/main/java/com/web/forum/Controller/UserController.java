package com.web.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.User;
import com.web.forum.Service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //Return an User by username
    @GetMapping("/get/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByName(username));
    }

    //Update user
    @PutMapping("/update/{oldUserName}")
    public ResponseEntity<?> updateUser(@PathVariable String oldUserName, @RequestBody User newUser) {
       return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(oldUserName, newUser));
    }

    //Delete an User by username
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(username));
    }
}
