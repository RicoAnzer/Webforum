package com.web.forum.Entity;

import com.web.forum.Roles;

//No JPA used in this project => repository and dao done manually
//Entity class for User
public class User {

    private String name;
    private String password;
    private Roles role;
    private String profileImagePath;
    private final String createdAt;
    private String deletedAt;
    private Boolean isBanned;

    //Constructor
    public User(String name, String password, Roles role, String profileImagePath, String createdAt, String deletedAt, Boolean isBanned) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.profileImagePath = profileImagePath;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.isBanned = isBanned;
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Roles getRole() {
        return role;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles setRole(Roles role) {
        return this.role = role;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }
}
