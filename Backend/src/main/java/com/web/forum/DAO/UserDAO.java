package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.Interfaces.IUserDAO;
import com.web.forum.Entity.Authentication.LoginCredentials;
import com.web.forum.Entity.User;
import com.web.forum.ForumApplication;

@Repository
public class UserDAO implements IUserDAO {

    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
    private final Connection connection;

    @Autowired
    private UserRoleDAO userRoleDAO;

    //Constructor
    public UserDAO(ForumApplication forumApplication) {
        //Connect to database;
        this.connection = forumApplication.connection;
    }

    //Save a new User to database
    @Override
    public ResponseEntity<String> create(User user, String password) {
        //SQL Statement to add new users to database
        String createSQL = "INSERT INTO users (name, password, profile_image_path, created_at, deleted_at, is_banned)"
                + "VALUES (?, ?, ?, ?, ?, ?);";

        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(createSQL)) {
            //At creation of User 
            //=> id is automatically created inside database, doesn't need to be set here
            //=> isBanned is false at default, doesn't need to be set here
            //=> deletedAt should be null at creation, set date only after User deleted account
            statement.setString(1, user.getName());
            statement.setString(2, password);
            statement.setString(3, user.getProfileImagePath());
            statement.setString(4, user.getCreatedAt());
            statement.setString(5, user.getDeletedAt());
            statement.setBoolean(6, user.getIsBanned());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create user");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User '" + user.getName() + "' created");
    }

    //Search specific User by using id as filter
    @Override
    public LoginCredentials readLoginCredentials(String username) {
        //SQL Statement to filter User where id equals parameter ID
        String readLoginSQL = "Select * FROM users WHERE name = ?;";
        //Initialize LoginCredentials object
        LoginCredentials credentials = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readLoginSQL)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                //For every entry...
                //...create new LoginCredentials Object
                credentials = new LoginCredentials(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("password")
                );
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return credentials;
    }

    //Search specific User by using id as filter
    @Override
    public User readName(String username) {
        //SQL Statement to filter User where name equals parameter username
        String readSQL = "Select * FROM users WHERE name = ?;";
        User user = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            //Create new User Object using results from statement above
            //For every entry...
            while (result.next()) {
                //...use placeholder List => must be later replaced using UserRoleRepository...
                List<String> roles = userRoleDAO.readById(result.getLong("id"));
                //...and create new User Object
                user = new User(
                        result.getLong("id"),
                        result.getString("name"),
                        roles,
                        result.getString("profile_image_path"),
                        result.getString("created_at"),
                        result.getString("deleted_at"),
                        result.getBoolean("is_Banned")
                );
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return user;
    }

    //Update existing User based on id
    @Override
    public ResponseEntity<?> update(User user) {
        //To change User:
        //=> User Object = Object containing changed settings
        //=> Fields id, profileImagePath and createdAt are unchangeable
        //SQL Statement to add new User to database
        String updateSQL = "UPDATE users SET name = ?, deleted_at = ?, is_banned = ? "
                + "WHERE id = ?;";
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getDeletedAt());
            statement.setBoolean(3, user.getIsBanned());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //Delete existing Topic based on id
    @Override
    public ResponseEntity<String> delete(String name) {
        //SQL Statement to delete User
        String deleteSQL = "DELETE FROM users WHERE name = ?;";
        //execute statement
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User '" + name + "' not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Deleted User '" + name + "'");
    }
}
