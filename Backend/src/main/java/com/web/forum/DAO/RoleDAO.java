package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.Interfaces.IRoleDAO;
import com.web.forum.Entity.Role;
import com.web.forum.ForumApplication;

@Repository
public class RoleDAO implements IRoleDAO {

    private static final Logger log = LoggerFactory.getLogger(TopicDAO.class);
    private final Connection connection;

    //Constructor
    public RoleDAO(ForumApplication forumApplication) {
        //Connect to database;
        this.connection = forumApplication.connection;
    }

    //Create and save new Role
    @Override
    public String create(String roleName) {
        //SQL Statement to add new roles to database
        String createSQL = "INSERT INTO roles (name)"
                + "VALUES (?);";
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(createSQL)) {
            //At creation of role 
            //=> id is automatically created inside database, doesn't need to be set here
            statement.setString(1, roleName);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return "Role '" + roleName + "' created";
    }

    //Find specific Role based on ID
    @Override
    public Role read(int ID) {
        //SQL Statement to filter Role where id equals parameter ID
        String readSQL = "Select * FROM roles WHERE id = ?;";
        //Role placeholder
        Role role = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setLong(1, ID);
            ResultSet result = statement.executeQuery();
            //Create new Role Object using results from statement above
            while (result.next()) {
                //For every entry...
                //...create new Role Object
                role = new Role(
                        result.getInt("id"),
                        result.getString("name")
                );
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return role;
    }

    //Delete existing Role
    @Override
    public String delete(String roleName) {
        //SQL Statement to delete Role
        String deleteSQL = "DELETE FROM roles WHERE name = ?;";
        //execute statement
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, roleName);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return "Role deleted";
    }
}
