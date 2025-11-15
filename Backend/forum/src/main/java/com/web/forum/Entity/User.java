package com.web.forum.Entity;
//No JPA used in this project => repository and dao done manually
public class User {

    private final Long id;
    private String name;
    private String password;
    private String role;
    private String profileImagePath;
    private final String createdAt;
    private String deletedAt;
    private Boolean isBanned;

    //Constructor
    public User(Long id, String name, String password, String role, String profileImagePath, String createdAt, String deletedAt, Boolean isBanned) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public String getRole() {
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

    public void setRole(String role) {
        this.role = role;
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
