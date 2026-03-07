package com.web.forum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.web.forum.Entity.Authentication.RegistrationRequest;
import com.web.forum.Entity.Post;
import com.web.forum.Entity.Thread;
import com.web.forum.Entity.Topic;
import com.web.forum.Entity.User;
import com.web.forum.Service.PostService;
import com.web.forum.Service.ThreadService;
import com.web.forum.Service.TopicService;
import com.web.forum.Service.UserService;

//Integration tests for PostControllerTests
//APPLICATION MUST RUN FOR TESTS TO BE SUCCESSFUL
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = true)
public class PostControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final PostService postService;
    private final TopicService topicService;
    private final ThreadService threadService;

    // mockPost1 is created and saved at setUp()
    private Post mockPost1;
    // mockPost2 is created and saved at createPostSuccess()
    private Post mockPost2;
    private User mockUser;
    private Thread mockThread;
    private Topic mockTopic;

    private static final Logger log = LoggerFactory.getLogger(PostControllerTests.class);

    @Autowired
    public PostControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService,
            PostService postService, TopicService topicService, ThreadService threadService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.postService = postService;
        this.topicService = topicService;
        this.threadService = threadService;
    }

    @BeforeAll
    public void setUp() {
        log.info("Start PostControllerTests...");
        topicService.deleteTopic("Test Topic in Post-Tests");
        userService.deleteUser("Franz1234");
        //topicService.createNewTopic("Test Topic in Post-Tests");
        //Create mock data
        userService.registerNewUser(new RegistrationRequest("Franz1234", "1234", "1234"));
        mockUser = userService.getUserByName("Franz1234");
        mockTopic = topicService.createNewTopic("Test Topic in Post-Tests");
        mockThread = threadService.createNewThread(mockTopic.getSlug(), "Test Thread in Post-Tests");


        // Format createdAt to "dd-MM-YYYY"
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        Long id = null;
        // Create content => JSON Object
        ObjectNode content = objectMapper.createObjectNode();
        content.put("text", "This is a test text");

        log.info(userService.getUserByName(mockUser.getName()).getId().toString());
        mockPost1 = postService.createPost(new Post(
                id,
                mockUser.getId(),
                mockThread.getSlug(),
                content,
                LocalDateTime.now().format(dateTimeFormatter),
                "",
                false));
    }

    @AfterAll
    public void cleanUp() {
        log.info("End PostControllerTests...");
        topicService.deleteTopic(mockTopic.getName());
        userService.deleteUser(mockUser.getName());
    }

    // Test successful Post creation
    @SuppressWarnings("null")
    @Order(1)
    @Test
    public void createPostSuccess() {
        log.info("Testing createPostSuccess()...");
        try {
            // Format createdAt to "dd-MM-YYYY"
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            Long id = null;
            // Create content => JSON Object
            ObjectNode content = objectMapper.createObjectNode();
            content.put("text", "This is another test text");

            mockPost2 = new Post(
                    id,
                    mockUser.getId(),
                    mockThread.getSlug(),
                    content,
                    LocalDateTime.now().format(dateTimeFormatter),
                    "",
                    false);

            RequestBuilder requestB = MockMvcRequestBuilders.post("/post/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockPost2));
            // Expect Status Created and check returned values
            mockMvc.perform(requestB)
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.text", is(mockPost2.getContent().get("text").asText())));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    // Test unsuccessful Post => content is empty
    @SuppressWarnings("null")
    @Order(2)
    @Test
    public void createPostWhenEmpty() {
        log.info("Testing createPostWhenEmpty()...");
        try {
            String errorMessage = "Content is empty";
            // Format createdAt to "dd-MM-YYYY"
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            Long id = null;
            // Create content => JSON Object
            ObjectNode content = objectMapper.createObjectNode();
            content.put("text", "");

            mockPost2 = new Post(
                    id,
                    mockUser.getId(),
                    mockThread.getSlug(),
                    content,
                    LocalDateTime.now().format(dateTimeFormatter),
                    "",
                    false);

            RequestBuilder requestB = MockMvcRequestBuilders.post("/post/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockPost2));

            // Expect Status Created and check returned values
            mockMvc.perform(requestB)
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    // Test getPostById when searched Post exists
    @SuppressWarnings("null")
    @Order(3)
    @Test
    public void getPostWhenExists() throws Exception {
        log.info("Testing getPostWhenExists()...");
        log.info(mockPost1.getId().toString());
        RequestBuilder request = MockMvcRequestBuilders.get("/post/{Id}", mockPost1.getId())
                .contentType(MediaType.APPLICATION_JSON);
        // Expect Status Ok and check returned content
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$.content.text", is(mockPost1.getContent().get("text").asText())));
    }

    // Test getPostById when searched Post doesn't exist
    @SuppressWarnings("null")
    @Order(4)
    @Test
    public void getPostWhenNotExists() throws Exception {
        String errorMessage = "Post not found";
        log.info("Testing getPostWhenNotExists()...");
        RequestBuilder request = MockMvcRequestBuilders.get("/post/{Id}", -1L)
                .contentType(MediaType.APPLICATION_JSON);
        // Expect Status Ok and check returned content
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));
    }

    // Test getAllPosts
    @SuppressWarnings("null")
    @Order(5)
    @Test
    public void getAllPosts() throws Exception {
        log.info("Testing getAllPosts()...");
        RequestBuilder request = MockMvcRequestBuilders.get("/post/all/{threadSlug}", mockThread.getSlug())
                .contentType(MediaType.APPLICATION_JSON);
        // Expect Status Ok and check returned content
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    // Test updatePost
    @SuppressWarnings("null")
    @Order(6)
    @Test
    public void updatePost() throws Exception {
        log.info("Testing updatePost()...");
        // Create updated User Object
        // Create content => JSON Object
        ObjectNode content = objectMapper.createObjectNode();
        content.put("text", "Updated Text");
        // Format createdAt to "dd-MM-YYYY"

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        Post updatePost = new Post(
                mockPost1.getId(),
                mockUser.getId(),
                mockThread.getSlug(),
                content,
                LocalDateTime.now().format(dateTimeFormatter),
                "",
                false);

        //RequestBody to update mockPost1
        RequestBuilder requestBupdate = MockMvcRequestBuilders.put("/post/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePost));

        //RequestBody to retrieve mockPost1 after update
        RequestBuilder requestBcheckUpdated = MockMvcRequestBuilders.get("/post/{Id}", mockPost1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // Expect Status Ok
        mockMvc.perform(requestBupdate)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Expect Status Ok and check if content was updated
        mockMvc.perform(requestBcheckUpdated)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.text", is(updatePost.getContent().get("text").asText())));
    }

    // Test deletePost
    @SuppressWarnings("null")
    @Order(7)
    @Test
    public void deletePost() throws Exception {
        log.info("Testing deletePost()...");
        String deleteMessage = "Deleted Post Number '" + mockPost1.getId() + "'";
        RequestBuilder request = MockMvcRequestBuilders.delete("/post/{postId}", mockPost1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // Expect Status Ok and "Deleted" message
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(deleteMessage));
    }
}
