package com.web.forum.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.forum.DAO.PostDAO;
import com.web.forum.Entity.Post;
import com.web.forum.Security.Error.CustomErrors.NotFoundError;

//PostService to manage database traffic of Posts (create, delete, etc...)
@Service
public class PostService {

    private final PostDAO postDAO;

    public PostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    // Create and save new Post
    public Post createPost(Post post){
        return postDAO.create(post);
    }

    // Find specific Post based on Id
    public Post getPostById(Long Id){
        if(postDAO.readId(Id) == null){
            throw new NotFoundError("Post not found");
        }
        return postDAO.readId(Id);
    }

    //Search and return Posts related to a specific Thread
    public List<Post> getAllThreads(String threadSlug) {
        return postDAO.readAll(threadSlug);
    }

    // Update existing Post
    public Post updatePost(Post updatedPost){
        return postDAO.update(updatedPost);
    }

    // Delete existing Post
    public String deletePost(Long Id){
        return postDAO.delete(Id);
    }
}
