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

import com.web.forum.DAO.TopicDAO;
import com.web.forum.Entity.Topic;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicDAO topicDAO;

    //Add a new Topic
    @PostMapping(value = {"/add/{name}", "/add/"})
    public ResponseEntity<?> addTopic(@PathVariable(required = false) String name) {
        if (name == null || name.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Topic is empty");
        }
        if (topicDAO.readbyName(name) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Topic with this name already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(topicDAO.create(name));
    }

    //Return Topic based on ID
    @GetMapping("/get/{ID}")
    public ResponseEntity<?> getTopic(@PathVariable Long ID) {
        Topic foundTopic = topicDAO.readbyID(ID);
        if (foundTopic != null) {
            return ResponseEntity.status(HttpStatus.OK).body(foundTopic);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Topic found");
        }
    }

    //Return all Topics
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTopics() {
        //Get a List of all Topics Objects
        List<Topic> topics = topicDAO.readAll();
        if (topics != null && !topics.isEmpty()) {
            //Return topics list
            return ResponseEntity.status(HttpStatus.OK).body(topics);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Topics found");
        }
    }

    //Deletes Topic by name
    @DeleteMapping("/delete/{topicName}")
    public ResponseEntity<?> deleteTopic(@PathVariable String topicName) {
        return ResponseEntity.status(HttpStatus.OK).body(topicDAO.delete(topicName));
    }
}
