package com.web.forum.Entity;

//Entity class for Topic (headline for Threads of similiar theme) => All information for Frontend
public class Topic {

    private final Long id;
    private final String name;

    //Constructor
    public Topic(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}