package com.web.forum.Entity.Authentication;

//Entity class for LoginCredentials => Login information used in backend for authentication
public class LoginCredentials {

    private Long userId;
    private String username;
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

    //Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
