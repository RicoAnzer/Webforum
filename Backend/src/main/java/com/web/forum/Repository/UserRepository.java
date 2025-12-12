package com.web.forum.Repository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.UserDAO;
import com.web.forum.Entity.User;

@Repository
public class UserRepository implements IUserRepository {

    //UserDAO
    private final UserDAO userDAO;

    //Constructor
    public UserRepository(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //Save a new User to database
    @Override
    public ResponseEntity<String> save(User user) {
        return userDAO.create(user);
    }

    //Show User in database by id
    @Override
    public User findById(Long ID) {
        return userDAO.read(ID);
    }

    //Show User in database by username
    @Override
    public User findByName(String username) {
        return userDAO.readName(username);
    }

    //Update existing User
    @Override
    public ResponseEntity<String> change(User user, Long userId) {
        return userDAO.update(user, userId);
    }

    //Delete existing User in database
    @Override
    public ResponseEntity<String> remove(Long ID) {
        return userDAO.delete(ID);
    }
    
}
