package com.web.forum.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Entity.Topic;
import com.web.forum.Repository.TopicRepository;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    //Add a new Topic
    @GetMapping("/add/{name}")
    public ResponseEntity<?> addTopic(@PathVariable String name) {

        try {
            //Add topic
            topicRepository.save(name);
        } catch (Exception e) {
            //If not matching => error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't add topic");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Topic " + name + " added");
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
            //If no topics found => error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No topics found");
        }
    }

    //Return Topic by ID
    @DeleteMapping("/get/{ID}")
    public ResponseEntity<?> getTopicsByID(@RequestParam Long ID) {
        //Get Topic by ID parameter
        Topic topic = topicRepository.findByID(ID);
        //If topic = null => topic doesn't exist
        if (topic != null) {
            //Return topic
            return ResponseEntity.status(HttpStatus.OK).body(topic);
        } else {
            //If topic = null => Error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find user '" + topic + "'");
        }
    }
}
