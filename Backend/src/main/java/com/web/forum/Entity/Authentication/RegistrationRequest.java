package com.web.forum.Entity.Authentication;

//Entity class for RegistrationRequest => Registration information used in backend for creation of User
public class RegistrationRequest {

    private final String username;
    private final String password;
    private final String confirmedPassword;

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
}