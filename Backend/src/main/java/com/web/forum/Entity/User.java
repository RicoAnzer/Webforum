package com.web.forum.Entity;

import java.util.List;

//Entity class for User => All User information for Frontend
public class User {

    private Long id;
    private String name;
    private List<String> roles;
    private String profileImagePath;
    private final String createdAt;
    private String deletedAt;
    private Boolean isBanned;

    //Constructor
    public User(Long id, String name, List<String> roles, String profileImagePath, String createdAt, String deletedAt, Boolean isBanned) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.profileImagePath = profileImagePath;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.isBanned = isBanned;
    }

    //Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return roles;
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
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> setRoles(List<String> roles) {
        return this.roles = roles;
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
