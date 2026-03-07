package com.web.forum.Entity;

import com.fasterxml.jackson.databind.JsonNode;

//Entity class for Post => All information to display posts
public class Post {

    private final Long id;
    private final Long userId;
    private final String thread_slug;
    // Content is JSON
    // => for example {text1: xyz, image1: path, text2: xyz} = Post containing text
    // paragraphs above and below an image
    private final JsonNode content;
    private final String created_at;
    private final String updated_at;
    private final boolean deleted;

    // Constructor
    public Post(Long id, Long userId, String thread_slug, JsonNode content, String created_at, String updated_at, boolean deleted) {
        this.id = id;
        this.userId = userId;
        this.thread_slug = thread_slug;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted = deleted;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getThread_slug() {
        return thread_slug;
    }

    public JsonNode getContent() {
        return content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean getDeleted() {
        return deleted;
    }
}
