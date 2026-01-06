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

import com.web.forum.Entity.Topic;
import com.web.forum.Repository.TopicRepository;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    //Add a new Topic
    @PostMapping("/add/{name}")
    public ResponseEntity<?> addTopic(@PathVariable String name) {
        return topicRepository.save(name);
    }

    //Return all Topics
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTopics() {
        //Get a List of all Topics Objects
        List<Topic> topics = topicRepository.findAll();
        if (topics != null && !topics.isEmpty()) {
            //Return topics list
            return ResponseEntity.status(HttpStatus.OK).body(topics);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Topics found");
        }
    }

    //Return Topic based on ID
    @GetMapping("/get/{ID}")
    public ResponseEntity<?> getTopic(@PathVariable Long ID) {
        Topic foundTopic = topicRepository.findByID(ID);
        if (foundTopic != null) {
            return ResponseEntity.status(HttpStatus.OK).body(foundTopic);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Topic found");
        }
    }

    //Deletes Topic by name
    @DeleteMapping("/delete/{topicName}")
    public ResponseEntity<?> deleteTopic(@PathVariable String topicName) {
        //Deletes Topic by topicName parameter
        return topicRepository.remove(topicName);
    }
}
