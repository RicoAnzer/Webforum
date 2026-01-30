package com.web.forum;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.web.forum.DAO.TopicDAO;

//Integration tests for RoleController
//APPLICATION MUST RUN FOR TESTS TO BE SUCCESSFUL
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = true)
public class RoleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final Logger log = LoggerFactory.getLogger(TopicDAO.class);

    @BeforeAll
    public void setUp(){
        log.info("Start RoleControllerTests...");
    }

    @AfterAll
    public void cleanUp(){
        log.info("End RoleControllerTests...");
    }

    //Test addRole
    @SuppressWarnings("null")
    @Order(1)
    @Test
    public void addRole() throws Exception {
        log.info("Testing addRole()...");
        String roleName = "Role1";
        String responseMessage = "Role '" + roleName + "' created";

        RequestBuilder request = MockMvcRequestBuilders.post("/role/add/{roleName}", roleName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Created and check created message
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string(responseMessage));
    }

    //Test deleteRole
    @SuppressWarnings("null")
    @Order(2)
    @Test
    public void deleteRole() throws Exception {
        log.info("Testing deleteRole()...");
        //Delete Role created in addRole()
        String roleName = "Role1";
        String deleteMessage = "Role deleted";

        RequestBuilder request = MockMvcRequestBuilders.delete("/role/delete/{username}", roleName)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and "Deleted" message
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(deleteMessage));
    }
}
