package com.web.forum.DAO.Interfaces;

import java.util.List;

import com.web.forum.Entity.Thread;

public interface IThreadDAO {
    
    //Create and save new Thread of a specific topic
    public Thread create(String topicSlug, Thread thread);

    //Find and return Thread in database based on ID
    public Thread readByName(String name);

    //Find and return all Threads of a specific topic
    public List<Thread> readAll(String topicSlug);

    //Delete existing Thread
    public String delete(String name);
}
