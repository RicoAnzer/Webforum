package com.web.forum.Entity.Authentication;

//Entity class for LoginCredentials => Login information used in backend for authentication
public class LoginCredentials {

    private final Long userId;
    private final String username;
    private final String password;

    //Constructor
    public LoginCredentials(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    //Getters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
