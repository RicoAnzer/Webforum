package com.web.forum.Entity;

//Entity class for Topic (headline for Threads of similiar theme) => All information for Frontend
public class Topic {

    private final Long id;
    private final String name;
    private final String slug;

    //Constructor
    public Topic(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    //Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }
}