package com.web.forum.DAO.Interfaces;

import org.springframework.http.ResponseEntity;

import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.User;

public interface IUserDAO {

    //Create and save new User
    public ResponseEntity<String> create(User user, String password);

    //Find loginCredentials based on username
    public LoginCredentials readLoginCredentials(String username);

    //Find specific User based on username
    public User readName(String username);

    //Update existing User
    public ResponseEntity<String> update(Long ID, User user);

    //Delete existing User
    public ResponseEntity<String> delete(String name);
}
