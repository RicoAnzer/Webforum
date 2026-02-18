package com.web.forum;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.forum.DAO.UserDAO;
import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.Authentication.RegistrationRequest;

import jakarta.servlet.http.Cookie;

//Integration tests for SignUpController
//APPLICATION MUST RUN FOR TESTS TO BE SUCCESSFUL
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = true)
public class SignUpControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    public UserDAO userDAO;

    private String registerRequestBody;
    private String loginRequestBody;
    private RegistrationRequest regRequest;
    private LoginCredentials credentials;

    private static final Logger log = LoggerFactory.getLogger(SignUpControllerTests.class);

    @BeforeAll
    public void setUp() {
        log.info("Start SignUpControllerTests...");
        this.regRequest = new RegistrationRequest("Alice1234", "1234", "1234");
        this.credentials = new LoginCredentials(1L, "Alice1234", "1234");
        //Create requestBody for testing
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //RegistrationRequest object as json
            registerRequestBody = objectMapper.writeValueAsString(regRequest);
            //LoginCredentials object as json
            loginRequestBody = objectMapper.writeValueAsString(credentials);
        } catch (JsonProcessingException ex) {
            log.error(ex.toString());
        }
    }

    @AfterAll
    public void cleanUp() {
        userDAO.delete(credentials.getUsername());
        log.info("End SignUpControllerTests...");
    }

    //Test successful user registration
    @SuppressWarnings("null")
    @Order(1)
    @Test
    public void registerUserSuccess() {
        log.info("Testing registerUserSuccess()...");
        try {
            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registerRequestBody);

            //Expect Status Created and check returned id
            mockMvc.perform(requestB)
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(regRequest.getUsername()));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user registration => Username already exists
    @SuppressWarnings("null")
    @Order(2)
    @Test
    public void registerUserUsernameExists() {
        log.info("Testing registerUserUsernameExists()...");
        try {
            String errorMessage = "Username already exists";

            //Attempting to register using the same credentials as in registerUserSuccess
            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registerRequestBody);

            //Expect Status BadRequest and check error message
            mockMvc.perform(requestB)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user registration => Username is empty
    @SuppressWarnings("null")
    @Order(3)
    @Test
    public void registerUserUsernameEmpty() {
        log.info("Testing registerUserUsernameEmpty()...");
        try {
            String errorMessage = "Username is empty";
            RegistrationRequest wrongRequest = new RegistrationRequest("", "1234", "1234");

            //False RegistrationRequest object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(wrongRequest);

            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status BadRequest and check error message
            mockMvc.perform(requestB)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user registration => Password is empty
    @SuppressWarnings("null")
    @Order(4)
    @Test
    public void registerUserUsernameTooLong() {
        log.info("Testing registerUserUsernameTooLong()...");
        try {
            String errorMessage = "Please choose an username with less than 20 characters";
            RegistrationRequest wrongRequest = new RegistrationRequest("Franz12345678901234567890", "1234", "1234");

            //False RegistrationRequest object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(wrongRequest);

            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status BadRequest and check error message
            mockMvc.perform(requestB)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user registration => Password is empty
    @SuppressWarnings("null")
    @Order(5)
    @Test
    public void registerUserPasswordEmpty() {
        log.info("Testing registerUserPasswordEmpty()...");
        try {
            String errorMessage = "Please choose a password";
            RegistrationRequest wrongRequest = new RegistrationRequest("Franz1234", "", "");

            //False RegistrationRequest object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(wrongRequest);

            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status BadRequest and check error message
            mockMvc.perform(requestB)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user registration => Password mismatch
    @SuppressWarnings("null")
    @Order(3)
    @Test
    public void registerUserPasswordMismatch() {
        log.info("Testing registerUserPasswordMismatch()...");
        try {
            RegistrationRequest wrongRequest = new RegistrationRequest("Dieter1234", "1234", "4321");
            String errorMessage = "Password and confirmedPassword don't match";

            //False RegistrationRequest object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(wrongRequest);

            RequestBuilder request = MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status BadRequest and check error message
            mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test successful user login
    @SuppressWarnings("null")
    @Order(4)
    @Test
    public void testUserLoginSuccess() {
        log.info("Testing testUserLoginSuccess()...");
        try {
            String cookieName = "jwtToken";

            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequestBody);

            //Expect Status Accepted and return response
            MvcResult result = mockMvc.perform(requestB)
                    .andExpect(status().isAccepted())
                    .andReturn();

            //Check if cookie created correctly
            assertThat(result.getResponse().getHeader("Set-Cookie").contains(cookieName));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user login => Log in, when already logged in
    @SuppressWarnings("null")
    @Order(5)
    @Test
    public void testUserLoginAlreadyLoggedIn() {
        log.info("Testing testUserLoginAlreadyLoggedIn()...");
        try {
            String cookieName = "jwtToken";
            String errorMessage = "User is already logged in";

            //RequestBody of first successful login
            RequestBuilder firstRequestB = MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequestBody);

            //First successful login: Expect Status Accepted and return response
            MvcResult firstLogin = mockMvc.perform(firstRequestB)
                    .andExpect(status().isAccepted())
                    .andReturn();
            //Exctract generated cookie out of response of first log in
            Cookie jwtToken = firstLogin.getResponse().getCookie(cookieName);

            //RequestBody of second failing login 
            //=> Add cookie of first log in to simulate user is already logged in when attempting again
            RequestBuilder secondRequestB = MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(jwtToken)
                    .content(loginRequestBody);

            //Expect Status Conflict and check error message
            mockMvc.perform(secondRequestB)
                    .andExpect(status().isConflict())
                    .andExpect(content().string(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user login => Log in using wrong username
    @SuppressWarnings("null")
    @Order(6)
    @Test
    public void testUserLoginWrongName() {
        String errorMessage = "User not found";
        log.info("Testing testUserLoginWrongName()...");
        try {
            LoginCredentials wrongCredentials = new LoginCredentials(1L, "Dieter4321", "1234");
            
            //False RegistrationRequest object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(wrongCredentials);

            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            //Expect Status BadRequest and check error message
            mockMvc.perform(requestB)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test unsuccessful user login => Wrong password used
    @SuppressWarnings("null")
    @Order(7)
    @Test
    public void testUserLoginWrongPassword() {
        log.info("Testing testUserLoginWrongPassword()...");
        try {
            String errorMessage = "Password not found";
            LoginCredentials wrongCredentials = new LoginCredentials(1L, "Alice1234", "4321");

            //wrongCredentials object as json
            ObjectMapper objectMapper = new ObjectMapper();
            String wrongloginRequestBody = objectMapper.writeValueAsString(wrongCredentials);

            RequestBuilder requestB = MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(wrongloginRequestBody);

            //Expect Status BadRequest and check error message
            mockMvc.perform(requestB)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));
                    
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test successful user logout
    @SuppressWarnings("null")
    @Order(8)
    @Test
    public void testUserLogoutSuccess() {
        log.info("Testing testUserLogoutSuccess()...");
        try {
            String cookieName = "jwtToken";
            String logOutMessage = "Logged out succesfully";

            //RequestBody of login
            RequestBuilder firstRequestB = MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequestBody);

            //First login: Expect Status Accepted and return response
            MvcResult firstLogin = mockMvc.perform(firstRequestB)
                    .andExpect(status().isAccepted())
                    .andReturn();
            //Exctract generated cookie out of response of first log in
            Cookie jwtToken = firstLogin.getResponse().getCookie(cookieName);

            //RequestBody of logout => Add cookie of log in to simulate user is logged in, when logging out
            RequestBuilder secondRequestB = MockMvcRequestBuilders.post("/auth/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(jwtToken)
                    .content(loginRequestBody);

            //Expect Status Ok and check logOut message
            mockMvc.perform(secondRequestB)
                    .andExpect(status().isOk())
                    .andExpect(content().string(logOutMessage));

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    //Test failed user logout => User is logging out, when not logged in
    @SuppressWarnings("null")
    @Order(9)
    @Test
    public void testUserLogoutNotLoggedIn() {
        log.info("Testing testUserLogoutNotLoggedIn()...");
        try {

            //RequestBody of logout => User is not logged in -> no Cookie added
            RequestBuilder secondRequestB = MockMvcRequestBuilders.post("/auth/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequestBody);

            //Expect Status Forbidden => User has no role "USER" and is blocked by Spring Security
            mockMvc.perform(secondRequestB)
                    .andExpect(status().isForbidden());

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }
}
