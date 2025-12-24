package com.web.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.User;
import com.web.forum.Repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //Return an User by username
    @GetMapping("/get/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        //Get User by ID parameter
        User user = userRepository.findByName(username);
        //If user = null => user doesn't exist
        if (user != null) {
            //Return User
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            //If user = null => Error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find user '" + user + "'");
        }
    }

    //Delete an User by username
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        userRepository.remove(username);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted User '" + username + "'");
    }
}
