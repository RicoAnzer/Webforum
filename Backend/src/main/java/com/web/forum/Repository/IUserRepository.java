package com.web.forum.Repository;

import org.springframework.http.ResponseEntity;

import com.web.forum.Entity.User;

//Interface for Repository of User class
public interface IUserRepository {
     //Create and save new User
    public ResponseEntity<String> save(User user);
    //Find specific User based on id
    public User findById(Long ID);
    //Update existing User
    public ResponseEntity<String> change(User user, Long userId);
    //Delete existing User
    public ResponseEntity<String> remove(Long ID);
}
