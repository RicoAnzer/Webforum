package com.web.forum.Repository.Interfaces;

import java.util.List;

import com.web.forum.Entity.Topic;

//Interface for Repository of Topic class
public interface ITopicRepository {

    //Create and save new Topic
    public String save(String name);

    //Find all Topics
    public List<Topic> findAll();

    //Find specific Topic by ID
    public Topic findByID(Long ID);

    //Find specific Topic by name
    public Topic findByName(String name);

    //Delete existing Topic
    public String remove(String name);
}
