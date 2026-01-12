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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User '" + username + "' not found");
        }
    }

    //Update user
    @PutMapping("/update/{oldUserName}")
    public ResponseEntity<?> updateUser(@PathVariable String oldUserName, @RequestBody User newUser) {
        User user = userRepository.findByName(oldUserName);
        //Check if user exists
        if (user != null) {
            //Check if new name already belongs to another user
            if (userRepository.findByName(newUser.getName()) == null) {
                return userRepository.change(newUser);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("An User with this name already exists");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User '" + oldUserName + "' not found");
        }
    }

    //Delete an User by username
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return userRepository.remove(username);
    }
}
