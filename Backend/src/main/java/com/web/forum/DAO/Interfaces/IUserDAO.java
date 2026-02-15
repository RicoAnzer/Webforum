package com.web.forum.DAO.Interfaces;

import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.User;

public interface IUserDAO {

    //Create and save new User
    public LoginCredentials create(User user, String password);

    //Find loginCredentials based on username
    public LoginCredentials readLoginCredentials(String username);

    //Find specific User based on username
    public User readName(String username);

    //Update existing User
    public User update(User user);

    //Delete existing User
    public String delete(String name);
}
