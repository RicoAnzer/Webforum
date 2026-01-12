package com.web.forum.Entity;

import java.util.List;

//Entity class for User => All User information for Frontend
public class User {

    private final Long id;
    private final String name;
    private final List<String> roles;
    private final String profileImagePath;
    private final String createdAt;
    private final String deletedAt;
    private final Boolean isBanned;

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
}