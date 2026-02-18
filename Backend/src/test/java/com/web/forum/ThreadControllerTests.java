package com.web.forum;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.web.forum.DAO.ThreadDAO;
import com.web.forum.DAO.TopicDAO;
import com.web.forum.Entity.Thread;
import com.web.forum.Entity.Topic;

//Integration tests for ThreadController
//APPLICATION MUST RUN FOR TESTS TO BE SUCCESSFUL
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = true)
public class ThreadControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public ThreadDAO threadDAO;

    @Autowired
    public TopicDAO topicDAO;

    private Topic mockTopic = null;

    private static final Logger log = LoggerFactory.getLogger(ThreadControllerTests.class);

    @BeforeAll
    public void setUp() {
        log.info("Start ThreadControllerTests...");
        topicDAO.create("First Test Topic");
        mockTopic = topicDAO.readbyName("First Test Topic");
        threadDAO.create("First Thread", mockTopic.getId());
        threadDAO.create("Second Thread", mockTopic.getId());
    }

    @AfterAll
    public void cleanUp() {
        log.info("End ThreadControllerTests...");
        threadDAO.delete("First Thread");
        threadDAO.delete("Second Thread");
        topicDAO.delete("First Test Topic");
    }


    //Test addThread when successful
    @SuppressWarnings("null")
    @Order(1)
    @Test
    public void addThread() throws Exception {
        log.info("Testing addThread()...");
        String threadName = "ThirdThread";

        RequestBuilder request = MockMvcRequestBuilders.post("/thread/add/{topicId}/{name}", mockTopic.getId(), threadName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Created and check created message
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(threadName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.topicId").value(mockTopic.getId()));
    }

    //Test addThread when Thread already exists
    @SuppressWarnings("null")
    @Order(2)
    @Test
    public void addThreadWhenExists() throws Exception {
        log.info("Testing addThreadWhenExists()...");
        String threadName = "ThirdThread";
        String errorMessage = "Thread with this name already exists";

        RequestBuilder request = MockMvcRequestBuilders.post("/thread/add/{topicId}/{name}", mockTopic.getId(), threadName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Conflict and check created message
        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(content().string(errorMessage));
    }

    //Test getAllThreads when Threads exist
    @SuppressWarnings("null")
    @Order(3)
    @Test
    public void getAllThreads() throws Exception {
        log.info("Testing getAllThreads()...");
        RequestBuilder request = MockMvcRequestBuilders.get("/thread/getAll/{topicId}", mockTopic.getId())
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and check returned ids
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    //Test getThread when Thread exist
    @SuppressWarnings("null")
    @Order(4)
    @Test
    public void getThreadWhenExists() throws Exception {
        log.info("Testing getThreadWhenExists()...");
        List<Thread> threads = threadDAO.readAll(mockTopic.getId());
        RequestBuilder request = MockMvcRequestBuilders.get("/thread/get/{threadName}", threads.get(0).getName())
                .contentType(MediaType.APPLICATION_JSON);
        //Expect Status Ok and check returned name
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(threads.get(0).getName()));
    }

    //Test getThread when Thread doesn't exist
    @SuppressWarnings("null")
    @Order(5)
    @Test
    public void getThreadWhenNotExists() throws Exception {
        log.info("Testing getThreadWhenNotExists()...");
        String errorMessage = "No Thread found";
        RequestBuilder request = MockMvcRequestBuilders.get("/thread/get/{threadName}","Non existent Thread name")
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status NotFound and check errorMessage
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    //Test deleteThread
    @SuppressWarnings("null")
    @Order(6)
    @Test
    public void deleteThread() throws Exception {
        log.info("Testing deleteThread()...");
        //Delete Topic created in Test addTopic()
        String threadName = "ThirdThread";
        String deleteMessage = "Thread deleted";

        RequestBuilder request = MockMvcRequestBuilders.delete("/thread/delete/{threadName}", threadName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and "Deleted" message
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(deleteMessage));
    }
}
