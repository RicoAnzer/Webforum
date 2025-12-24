package com.web.forum.Repository.Interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.web.forum.Entity.Topic;

//Interface for Repository of Topic class
public interface ITopicRepository {

    //Create and save new Topic
    public ResponseEntity<String> save(String name);

    //Find all Topics
    public List<Topic> findAll();

    //Find specific Topic by ID
    public Topic findByID(Long ID);

    //Delete existing Topic
    public ResponseEntity<String> remove(String name);
}
