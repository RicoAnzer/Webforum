package com.web.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Security.Error.CustomErrors.ConflictError;
import com.web.forum.Service.TopicService;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    //Add a new Topic
    @PostMapping(value = {"/add/{name}", "/add/"})
    public ResponseEntity<?> addTopic(@PathVariable(required = false) String name) {
        if (name == null || name.trim().isEmpty()){
            throw new ConflictError("Topic is empty");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createNewTopic(name));
    }

    //Return Topic based on ID
    @GetMapping("/get/{ID}")
    public ResponseEntity<?> getTopic(@PathVariable Long ID) {
        return  ResponseEntity.status(HttpStatus.OK).body(topicService.getTopicById(ID));
    }

    //Return all Topics
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTopics() {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getAllTopics()); 
    }

    //Deletes Topic by name
    @DeleteMapping("/delete/{topicName}")
    public ResponseEntity<?> deleteTopic(@PathVariable String topicName) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.deleteTopic(topicName));
    }
}
