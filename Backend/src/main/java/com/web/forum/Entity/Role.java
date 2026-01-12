package com.web.forum.Entity;

//Entity class for Role of User
public class Role {

    private final int id;
    private final String name;

    //Constructor
    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}