package com.web.forum.Entity;

//Entity class for Thread
public class Thread {

    private final Long id;
    private final Long topicId;
    private final String name;
    private final String slug;

    //Constructor
    public Thread(Long id, Long topicId, String name, String slug) {
        this.id = id;
        this.topicId = topicId;
        this.name = name;
        this.slug = slug;
    }

    //Getters
    public Long getId() {
        return id;
    }
    public Long getTopicId() {
        return topicId;
    }
    public String getName() {
        return name;
    }
    public String getSlug() {
        return slug;
    } 
}
