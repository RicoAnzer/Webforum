package com.web.forum.Repository.Interfaces;

import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.User;

//Interface for Repository of User class
public interface IUserRepository {

    //Create and save new User
    public LoginCredentials save(User user, String password);

    //Find specific User based on username
    public LoginCredentials findCredByName(String username);

    //Find specific User based on username
    public User findByName(String username);

    //Update existing User
    public User change(User user);

    //Delete existing User
    public String remove(String username);
}
