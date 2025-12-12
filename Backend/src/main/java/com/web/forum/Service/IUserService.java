package com.web.forum.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.web.forum.Entity.User;
import com.web.forum.Roles;

//Interface for UserService
public interface IUserService {
    //Register a new User
    public User registerNewUser(String name, String password, Enum<Roles> roles);
    //User login
    public UserDetails loadUserByUsername(String username);
    //Encoding user password
	public PasswordEncoder passwordEncoder();
}
