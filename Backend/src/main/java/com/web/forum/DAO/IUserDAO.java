package com.web.forum.DAO;

import org.springframework.http.ResponseEntity;

import com.web.forum.Entity.User;

public interface IUserDAO {
    //Create and save new User
    public ResponseEntity<String> create(User user);
    //Find specific User based on id
    public User read(Long ID);
    //Find specific User based on username
    public User readName(String username);
    //Update existing User
    public ResponseEntity<String> update(User user, Long userId);
    //Delete existing User
    public ResponseEntity<String> delete(Long ID);
}
