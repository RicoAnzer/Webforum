package com.web.forum.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.TopicDAO;
import com.web.forum.Entity.Topic;
import com.web.forum.Repository.Interfaces.ITopicRepository;

@Repository
public class TopicRepository implements ITopicRepository {

    @Autowired
    private TopicDAO topicDAO;

    //Save a new Topic to database
    @Override
    public String save(String name) {
        return topicDAO.create(name);
    }

    //Return all Topics in database
    @Override
    public List<Topic> findAll() {
        return topicDAO.readAll();
    }

    //Find specific Topic by ID
    @Override
    public Topic findByID(Long ID) {
        return topicDAO.readbyID(ID);
    } 

    //Find specific Topic by ID
    @Override
    public Topic findByName(String name) {
        return topicDAO.readbyName(name);
    } 

    //Delete existing Topic in database
    @Override
    public String remove(String name) {
        return topicDAO.delete(name);
    }
}
