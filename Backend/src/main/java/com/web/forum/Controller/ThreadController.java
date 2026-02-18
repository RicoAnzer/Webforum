package com.web.forum.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.DAO.ThreadDAO;
import com.web.forum.Entity.Thread;

@RestController
@RequestMapping("/thread")
public class ThreadController {

    @Autowired
    private ThreadDAO threadDAO;

    //Add a new Thread
    @PostMapping("/add/{topicId}/{threadName}")
    public ResponseEntity<?> addThread(@PathVariable String threadName, @PathVariable Long topicId) {
        if (threadDAO.readByName(threadName) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Thread with this name already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(threadDAO.create(threadName, topicId));
    }

    //Return Thread based on name
    @GetMapping("/get/{threadName}")
    public ResponseEntity<?> getThread(@PathVariable String threadName) {
        Thread foundThread = threadDAO.readByName(threadName);
        if (foundThread != null) {
            return ResponseEntity.status(HttpStatus.OK).body(foundThread);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Thread found");
        }
    }

    //Return all Threads of a specific Topic
    @GetMapping("/getAll/{topicId}")
    public ResponseEntity<?> getAllThreads(@PathVariable Long topicId) {
        //Get a List of all Threads Objects of a specific Topic
        List<Thread> threads = threadDAO.readAll(topicId);
        if (threads != null && !threads.isEmpty()) {
            //Return threads list
            return ResponseEntity.status(HttpStatus.OK).body(threads);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Threads found");
        }
    }

    //Deletes Thread by name
    @DeleteMapping("/delete/{threadName}")
    public ResponseEntity<?> deleteThread(@PathVariable String threadName) {
        return ResponseEntity.status(HttpStatus.OK).body(threadDAO.delete(threadName));
    }
}
