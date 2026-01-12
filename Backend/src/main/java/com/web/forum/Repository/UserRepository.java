package com.web.forum.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.UserDAO;
import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.User;
import com.web.forum.Repository.Interfaces.IUserRepository;

@Repository
public class UserRepository implements IUserRepository {

    @Autowired
    private UserDAO userDAO;

    //Save a new User to database
    @Override
    public ResponseEntity<String> save(User user, String password) {
        return userDAO.create(user, password);
    }

    @Override
    //Show User in database by username
    public LoginCredentials findCredByName(String username) {
        return userDAO.readLoginCredentials(username);
    }

    //Show User in database by username
    @Override
    public User findByName(String username) {
        return userDAO.readName(username);
    }

    //Update existing User
    @Override
    public ResponseEntity<?> change(User user) {
        return userDAO.update(user);
    }

    //Delete existing User in database
    @Override
    public ResponseEntity<String> remove(String username) {
        return userDAO.delete(username);
    }
}
