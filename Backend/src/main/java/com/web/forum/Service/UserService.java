package com.web.forum.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.forum.Entity.User;
import com.web.forum.Repository.UserRepository;
import com.web.forum.Roles;

//UserService to manage Users
@Service
public class UserService implements UserDetailsService {

    //UserRepository
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    //Register a new User
    public User registerNewUser(String name, String password, Roles roles)
    {
        //Format createdAt to "dd-MM-YYYY"
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        //Create User Object
        User newUser = new User(
            name, 
            password, 
            roles,
            "",
            LocalDate.now().format(dateTimeFormatter),
            "",
            false
        );
        //Save newUser to database
        userRepository.save(newUser);
        //Return created User
        return newUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }
}
