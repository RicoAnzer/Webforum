package com.web.forum.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Security.Error.CustomErrors.BadRequestError;
import com.web.forum.Service.ThreadService;

@RestController
@RequestMapping("/thread")
public class ThreadController {

    private final ThreadService threadService;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    //Add a new Thread
    @PostMapping(value = {"/add/{topicSlug}/{threadName}", "/add/{topicSlug}/"})
    public ResponseEntity<?> addThread(@PathVariable String topicSlug, @PathVariable(required = false) String threadName) {
        //Validating input (threadName) => topicSlug is always existing, otherwise no Thread can be added
        if (threadName == null || threadName.trim().isEmpty()) {
            throw new BadRequestError("Thread name is empty");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(threadService.createNewThread(topicSlug, threadName));
    }

    //Return Thread based on name
    @GetMapping("/get/{threadName}")
    public ResponseEntity<?> getThread(@PathVariable String threadName) {
        return ResponseEntity.status(HttpStatus.OK).body(threadService.getThreadByName(threadName));
    }

    //Return all Threads of a specific Topic
    @GetMapping("/getAll/{topicSlug}")
    public ResponseEntity<?> getAllThreads(@PathVariable String topicSlug) {
        return ResponseEntity.status(HttpStatus.OK).body(threadService.getAllThreads(topicSlug));
    }

    //Deletes Thread by name
    @DeleteMapping("/delete/{threadName}")
    public ResponseEntity<?> deleteThread(@PathVariable String threadName) {
        return ResponseEntity.status(HttpStatus.OK).body(threadService.deleteThread(threadName));
    }
}
