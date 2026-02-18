package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.Interfaces.IUserRoleDAO;
import com.web.forum.Entity.Role;
import com.web.forum.ForumApplication;

@Repository
public class UserRoleDAO implements IUserRoleDAO {

    private static final Logger log = LoggerFactory.getLogger(UserRoleDAO.class);
    private final Connection connection;

    @Autowired
    private RoleDAO roleDAO;

    //Constructor
    public UserRoleDAO(ForumApplication forumApplication) {
        //Connect to database;
        this.connection = forumApplication.connection;
    }

    //Create and save new user role
    @Override
    public ResponseEntity<String> create(Long userID, int roleID) {
        //SQL Statement to add new roles to database
        String createSQL = "INSERT INTO user_roles (user_id, role_id)"
                + "VALUES (?, ?);";

        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(createSQL)) {
            //Creation of user role 
            statement.setLong(1, userID);
            statement.setLong(2, roleID);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create user role");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User connection created");
    }

    //Find all Roles of specific User by id
    @Override
    public List<String> readById(Long userID) {
        //SQL Statement to filter all user_role entries tied to userID
        String readSQL = "Select * FROM user_roles WHERE user_id = ?;";
        //List containing all roles of user
        List<String> roles = new ArrayList<>();
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setLong(1, userID);
            ResultSet result = statement.executeQuery();
            //For every found database entry...
            while (result.next()) {
                //...extract the role based on role_id...
                Role role = roleDAO.read(result.getInt("role_id"));
                //...and add to roles List
                roles.add(role.getName());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return roles;
    }
}
