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

import com.web.forum.DAO.UserDAO;
import com.web.forum.Entity.User;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    //Return an User by username
    @GetMapping("/get/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        //Get User by ID parameter
        User user = userDAO.readName(username);
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
        User user = userDAO.readName(oldUserName);
        //Check if user exists
        if (user != null) {
            //Check if new name already belongs to another user
            if (userDAO.readName(newUser.getName()) == null) {
                return ResponseEntity.status(HttpStatus.OK).body(userDAO.update(newUser));
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
        return ResponseEntity.status(HttpStatus.OK).body(userDAO.delete(username));
    }
}
