package com.web.forum.Entity.Authentication;

//Entity class for RegistrationRequest => Registration information used in backend for creation of User
public class RegistrationRequest {

    private String username;
    private String password;
    private String confirmedPassword;

    //Constructor
    public RegistrationRequest(String username, String password, String confirmedPassword) {
        this.username = username;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }

    //Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    //Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }
}
