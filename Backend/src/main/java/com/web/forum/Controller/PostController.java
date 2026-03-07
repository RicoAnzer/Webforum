package com.web.forum.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.Post;
import com.web.forum.Security.Error.CustomErrors.BadRequestError;
import com.web.forum.Security.Error.CustomErrors.NotFoundError;
import com.web.forum.Service.PostService;


@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //Add new Post
    @PostMapping("/")
    public ResponseEntity<?> addPost(@RequestBody Post post) {
        if ("".equals(post.getContent().get("text").asText())) {
            throw new BadRequestError("Content is empty");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post));
    }

    //Search and return Post based on ID
    @GetMapping("/{Id}")
    public Post getPostById(@PathVariable Long Id) {
        Post foundPost = postService.getPostById(Id);
        if (foundPost == null) {
            throw new NotFoundError("No Post found");
        }
        return foundPost;
    }

    //Search and return Posts related to a specific Thread
    @GetMapping("/all/{threadSlug}")
    public List<Post> getAllPosts(@PathVariable String threadSlug) {
        return postService.getAllThreads(threadSlug);
    }

    //Update Post
    @PutMapping("/")
    public ResponseEntity<?> updatePost(@RequestBody Post updatedPost) {
       return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(updatedPost));
    }

    //Delete existing Post
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId));
    }
}
