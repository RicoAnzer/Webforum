package com.web.forum.Entity;

//Entity class for Thread
public class Thread {

    private final Long id;
    private final String topicSlug;
    private final String name;
    private final String slug;

    //Constructor
    public Thread(Long id, String topicSlug, String name, String slug) {
        this.id = id;
        this.topicSlug = topicSlug;
        this.name = name;
        this.slug = slug;
    }

    //Getters
    public Long getId() {
        return id;
    }
    public String getTopicSlug() {
        return topicSlug;
    }
    public String getName() {
        return name;
    }
    public String getSlug() {
        return slug;
    } 
}
