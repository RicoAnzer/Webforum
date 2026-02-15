package com.web.forum.DAO.Interfaces;

import java.util.List;

import com.web.forum.Entity.Topic;

public interface ITopicDAO {

    //Create and save new Topic
    public Topic create(String name);

    //Find and return all Topics in database
    public List<Topic> readAll();

    //Find and return Topic in database based on ID
    public Topic readbyID(Long ID);

    //Find and return Topic in database based on name
    public Topic readbyName(String name);

    //Delete existing Topic
    public String delete(String name);
}
