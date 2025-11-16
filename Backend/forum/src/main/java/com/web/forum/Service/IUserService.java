package com.web.forum.Service;

import com.web.forum.Entity.User;
import com.web.forum.Roles;

//Interface for UserService
public interface IUserService {
    //Register a new User
    public User registerNewUser(String name, String password, Enum<Roles> roles);
}
