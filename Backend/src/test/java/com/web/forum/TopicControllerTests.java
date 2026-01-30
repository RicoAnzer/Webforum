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

import com.web.forum.DAO.TopicDAO;
import com.web.forum.Entity.Topic;
import com.web.forum.Repository.TopicRepository;

//Integration tests for TopicController
//APPLICATION MUST RUN FOR TESTS TO BE SUCCESSFUL
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = true)
public class TopicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public TopicRepository topicRepository;

    private static final Logger log = LoggerFactory.getLogger(TopicDAO.class);

    @BeforeAll
    public void setUp() {
        log.info("Start TopicControllerTests...");
        topicRepository.save("First Topic");
        topicRepository.save("Second Topic");
    }

    @AfterAll
    public void cleanUp() {
        log.info("End TopicControllerTests...");
        topicRepository.remove("First Topic");
        topicRepository.remove("Second Topic");
    }

    //Test addTopic when successful
    @SuppressWarnings("null")
    @Order(1)
    @Test
    public void addTopic() throws Exception {
        log.info("Testing addTopic()...");
        String topicName = "ThirdTopic";
        String roleAddResponse = "Topic '" + topicName + "' created";

        RequestBuilder request = MockMvcRequestBuilders.post("/topic/add/{name}", topicName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Created and check created message
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string(roleAddResponse));
    }

    //Test addTopic when topic already exists
    @SuppressWarnings("null")
    @Order(2)
    @Test
    public void addTopicWhenExists() throws Exception {
        log.info("Testing addTopicWhenExists()...");
        String topicName = "ThirdTopic";
        String errorMessage = "Topic with this name already exists";

        RequestBuilder request = MockMvcRequestBuilders.post("/topic/add/{name}", topicName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Created and check created message
        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(content().string(errorMessage));
    }

    //Test getAllTopics when Topics exist
    @SuppressWarnings("null")
    @Order(3)
    @Test
    public void getAllTopics() throws Exception {
        log.info("Testing getAllTopics()...");
        RequestBuilder request = MockMvcRequestBuilders.get("/topic/getAll")
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and check returned ids
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    //Test getAllTopics when Topic exist
    @SuppressWarnings("null")
    @Order(4)
    @Test
    public void getTopicWhenExists() throws Exception {
        log.info("Testing getTopicWhenExists()...");
        List<Topic> topics = topicRepository.findAll();

        RequestBuilder request = MockMvcRequestBuilders.get("/topic/get/{ID}", topics.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and check returned name
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(topics.get(0).getName()));
    }

    //Test getAllTopics when Topic doesn't exist
    @SuppressWarnings("null")
    @Order(5)
    @Test
    public void getTopicWhenNotExists() throws Exception {
        log.info("Testing getTopicWhenNotExists()...");
        RequestBuilder request = MockMvcRequestBuilders.get("/topic/get/{ID}",-1)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and check returned name
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    //Test deleteTopic
    @SuppressWarnings("null")
    @Order(6)
    @Test
    public void deleteTopic() throws Exception {
        log.info("Testing deleteTopic()...");
        //Delete Topic created in Test addTopic()
        String topicName = "ThirdTopic";
        String deleteMessage = "Topic deleted";

        RequestBuilder request = MockMvcRequestBuilders.delete("/topic/delete/{topicName}", topicName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and "Deleted" message
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(deleteMessage));
    }
}
