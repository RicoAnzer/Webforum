package com.web.forum.Entity.Authentication;

//Entity class for LoginCredentials => Login information used in backend for authentication
public class LoginCredentials {

    private Long userId;
    private final String username;
    private String password;

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

    public void setId(Long Id) {
        this.userId = Id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
