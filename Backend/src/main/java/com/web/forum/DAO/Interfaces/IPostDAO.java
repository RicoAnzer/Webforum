package com.web.forum.DAO.Interfaces;

import java.util.List;

import com.web.forum.Entity.Post;

public interface  IPostDAO {

    //Create and save new Post
    public Post create(Post post);

    //Find specific Post based on Id
    public Post readId(Long Id);

    //Search and return Posts related to a sepcific Thread
    public List<Post> readAll(String threadSlug);

    //Update existing Post
    public Post update(Post updatedPost);

    //Delete existing Post
    public String delete(Long Id);
}
