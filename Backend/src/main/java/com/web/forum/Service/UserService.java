package com.web.forum.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.forum.DAO.RoleDAO;
import com.web.forum.DAO.UserDAO;
import com.web.forum.DAO.UserRoleDAO;
import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.Authentication.RegistrationRequest;
import com.web.forum.Entity.Role;
import com.web.forum.Security.Error.CustomErrors.BadRequestError;
import com.web.forum.Security.Error.CustomErrors.ConflictError;
import com.web.forum.Security.Error.CustomErrors.NotFoundError;
import com.web.forum.Security.Error.CustomErrors.UnauthorizedError;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//UserService to manage database traffic of Users (SignUp , login, get Users, etc...)
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private JwtService jwtService;

    // Logger
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // User login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find User by username
        LoginCredentials credentials = userDAO.readLoginCredentials(username);
        // LoginCredentials == null => No User with this username, throw error
        if (credentials == null) {
            throw new UsernameNotFoundException("User not found with this name: " + username);
        }
        // List all roles
        List<String> roles = userRoleDAO.readById(credentials.getUserId());

        // Create userdetails
        UserDetails user = User
                .withUsername(credentials.getUsername())
                .password(credentials.getPassword())
                .roles(roles.toArray(String[]::new))
                .build();
        // Return UserDetails
        return user;
    }

    // Register new user
    public LoginCredentials registerNewUser(RegistrationRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        
        // Check if username already exists (if it doesn't exist: readName() == null)
        if (userDAO.readName(username) == null) {
            // Encode password
            String encodedPassword = passwordEncoder().encode(password);
            // Format createdAt to "dd-MM-YYYY"
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            // Id null to auto generate id at database
            Long id = null;

            // Empty roles List as placeholder
            // => Roles List shows all roles assigned to user for later use in Frontend
            // (E.g. Only Moderators can add Topics, so Button to add them is invisible for
            // common users)
            // => User and Roles getting assigned in later step in table user_roles
            List<String> roles = new ArrayList<>();

            // Create User Object
            com.web.forum.Entity.User newUser = new com.web.forum.Entity.User(
                    id,
                    username,
                    roles,
                    "",
                    LocalDate.now().format(dateTimeFormatter),
                    "",
                    false);

            // Save newUser to database and autogenerate ID
            LoginCredentials credentials = userDAO.create(newUser, encodedPassword);
            // Bind Role 'USER' to created user in table user_roles
            try {
                // Extract Role "USER"
                // => 1 = USER
                if (credentials != null) {
                    Role role = roleDAO.read(1);
                    // Bind Role "USER" to user
                    userRoleDAO.create(credentials.getUserId(), role.getId());
                }
            } catch (Exception e) {
                throw new ConflictError("Can't bind User to Role");
            }

            return credentials;

        } else {
            // If name already in database => error
            throw new BadRequestError("Username already exists");
        }
    }

    // Login as user
    public com.web.forum.Entity.User userLogin(@RequestBody LoginCredentials loginCredentials,
            HttpServletRequest request, HttpServletResponse response) {
        // Load user Object from database
        com.web.forum.Entity.User loggedInUser = userDAO.readName(loginCredentials.getUsername());
        // If user doesn't exist in database => error
        if (loggedInUser == null) {
            throw new BadRequestError("User not found");
        }
        // If user is already logged in => error
        if (jwtService.isLoggedIn(request)) {
            throw new ConflictError("User is already logged in");
        }
        // If password is wrong => error
        String encryptedPassword = userDAO.readLoginCredentials(loginCredentials.getUsername()).getPassword();
        if (!passwordEncoder().matches(loginCredentials.getPassword(), encryptedPassword)) {
            throw new BadRequestError("Password not found");
        }

        // Expiry interval for cookie = 1 Day in Milliseconds
        int expiryInterval = 24 * 60 * 60 * 1000;
        // If username and password are correct => generate jwt token
        String token = jwtService.generateToken(loginCredentials.getUsername(), expiryInterval);

        log.info("Log in success: " + loginCredentials.getUsername());

        // Send JWT token in an HTTP-Only cookie
        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expiryInterval)
                .sameSite("none")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return loggedInUser;
    }

    // Logout user by deleting jwt token
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Check for jwt token in cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    // Extract jwt token
                    String jwtToken = cookie.getValue();
                    // Validate jwt token and delete if validated
                    if (jwtToken != null
                            || !jwtService.isTokenExpired(jwtToken)
                                    && userDAO.readName(jwtService.getUserName(jwtToken)) != null) {
                        // Create new cookie with same name and maxAge to 0 => delete Cookie
                        ResponseCookie newCookie = ResponseCookie.from("jwtToken", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("none")
                                .build();
                        response.addHeader("Set-Cookie", newCookie.toString());
                        log.info("Log out success: " + jwtService.getUserName(jwtToken));
                        return "Logged out succesfully";
                    }
                }
            }
        }
        // No token found => Not logged in
        throw new UnauthorizedError("Not logged in");
    }

    //Return an User by username
    public com.web.forum.Entity.User getUserByName(String username) {
        com.web.forum.Entity.User user = userDAO.readName(username);
        if (user == null) {
            throw new NotFoundError("User '" + username + "' not found");
        }
        return user;
    }

    //Update user
    public com.web.forum.Entity.User updateUser(String oldName, com.web.forum.Entity.User newUser) {
        com.web.forum.Entity.User user = userDAO.readName(oldName);
        if (user == null) {
            throw new NotFoundError("User '" + oldName + "' not found");
        }
        if (userDAO.readName(newUser.getName()) != null) {
            throw new ConflictError("An User with this name already exists");

        }
        return userDAO.update(newUser);
    }

    //Delete an User by username
    public String deleteUser(String username) {
        return userDAO.delete(username);
    }
}
