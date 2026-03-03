package com.web.forum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.forum.Entity.Authentication.RegistrationRequest;
import com.web.forum.Entity.User;
import com.web.forum.Service.UserService;

//Integration tests for UserController
//APPLICATION MUST RUN FOR TESTS TO BE SUCCESSFUL
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = true)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    String newName = "";
    RegistrationRequest mockUser = null;
    RegistrationRequest mockUser2 = null;

    private static final Logger log = LoggerFactory.getLogger(UserControllerTests.class);

    @BeforeAll
    public void setUp() {
        log.info("Start UserControllerTests...");
        //Create mocked user
        mockUser = new RegistrationRequest("Herbert1234", "1234", "1234");
        mockUser2 = new RegistrationRequest("Franz1234", "1234", "1234");
        userService.registerNewUser(mockUser);
        userService.registerNewUser(mockUser2);
        //New name of mockUser for testing updateUserWhenExists()
        newName = "Herbert4321";
    }

    @AfterAll
    public void cleanUp(){
        log.info("End UserControllerTests...");
    }

    //Test getUser when searched User exists
    @SuppressWarnings("null")
    @Order(1)
    @Test
    public void getUserWhenExists() throws Exception {
        log.info("Testing getUserWhenExists()...");
        RequestBuilder request = MockMvcRequestBuilders.get("/user/get/{username}", mockUser.getUsername())
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status Ok and check returned name
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockUser.getUsername()));
    }

    //Test getUser when searched User doesn't exist
    @SuppressWarnings("null")
    @Order(2)
    @Test
    public void getUserWhenNotExists() throws Exception {
        log.info("Testing getUserWhenNotExists()...");
        String username = "Alice4321";
        String errorMessage = "User '" + username + "' not found";
        RequestBuilder request = MockMvcRequestBuilders.get("/user/get/{username}", username)
                .contentType(MediaType.APPLICATION_JSON);

        //Expect Status NotFound and error message
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));
    }

    //Test updateUser when updated user exists
    @SuppressWarnings("null")
    @Order(3)
    @Test
    public void updateUserWhenExists() throws Exception {
        log.info("Testing updateUserWhenExists()...");
        try {
            List<String> roles = new ArrayList<>(Arrays.asList("USER"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String date = LocalDate.now().format(dateTimeFormatter);
            User oldUser = userService.getUserByName(mockUser.getUsername());
            User updatedUser = new User(oldUser.getId(), newName, roles, "null", date, "", false);

            //UpdatedUser object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(updatedUser);

            RequestBuilder request = MockMvcRequestBuilders.put("/user/update/{oldUserName}", oldUser.getName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status Ok and check name of updated user
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedUser.getName()));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test updateUser when updated user doesn't exist
    @SuppressWarnings({"null"})
    @Order(4)
    @Test
    public void updateUserWhenNotExists() throws Exception {
        log.info("Testing updateUserWhenNotExists()...");
        try {
            String username = "Peter1234";
            User mockedUser = userService.getUserByName(mockUser.getUsername());
            User updatedUser = new User(mockedUser.getId(), username, null, null, null, null, false);
            String errorMessage = "User '" + username + "' not found";

            //UpdatedUser object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(updatedUser);

            RequestBuilder request = MockMvcRequestBuilders.put("/user/update/{oldUserName}", username)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status Ok and check name of updated user
            mockMvc.perform(request)
                    .andExpect(status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test updateUser when the new name belongs to another user
    @SuppressWarnings({"null"})
    @Order(5)
    @Test
    public void updateUserWhenNewNameExists() throws Exception {
        log.info("Testing updateUserWhenNewNameExists()...");
        try {
            //mockUser2 (Franz) tries to rename his account to the new name of mockUser (Herbert)
            //Get Herbert
            User updatedUser = userService.getUserByName(newName);
            String errorMessage = "An User with this name already exists";

            //UpdatedUser object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(updatedUser);

            RequestBuilder request = MockMvcRequestBuilders.put("/user/update/{oldUserName}", mockUser2.getUsername())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status Ok and check name of updated user
            mockMvc.perform(request)
                    .andExpect(status().isConflict())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test deleteUser
    @SuppressWarnings("null")
    @Order(6)
    @Test
    public void deleteUser() throws Exception {
        log.info("Testing deleteUser()...");
        String deleteMessage1 = "Deleted User '" + newName + "'";
        String deleteMessage2 = "Deleted User '" + mockUser2.getUsername() + "'";

        //Delete Herbert using his new name after updating
        RequestBuilder request1 = MockMvcRequestBuilders.delete("/user/delete/{username}", newName)
                .contentType(MediaType.APPLICATION_JSON);
        //Delete Franz
        RequestBuilder request2 = MockMvcRequestBuilders.delete("/user/delete/{username}", mockUser2.getUsername())
                .contentType(MediaType.APPLICATION_JSON);

        //Delete Herbert and Franz created in setUp()
        //Expect Status Ok and "Deleted" message
        mockMvc.perform(request1)
                .andExpect(status().isOk())
                .andExpect(content().string(deleteMessage1));

        //Expect Status Ok and "Deleted" message
        mockMvc.perform(request2)
                .andExpect(status().isOk())
                .andExpect(content().string(deleteMessage2));
    }
}
