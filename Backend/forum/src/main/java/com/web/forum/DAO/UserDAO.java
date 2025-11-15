package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.web.forum.Database.DatabaseManager;
import com.web.forum.Entity.User;

public class UserDAO implements IUserDAO {

    //Logger
    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
    private final Connection connection;

    //Constructor
    public UserDAO(DatabaseManager databaseManager)
    {
        connection = databaseManager.connection;
    }

    //Save a new Article to database
    @Override
    public ResponseEntity<String> create(User user) {
        //SQL Statement to add new users to database
        String createSQL = "INSERT INTO users (name, password, role, profileImagePath, deletedAt)" +
               "VALUES (?, ?, ?, ?, ?);";

        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(createSQL)) 
        {
            //At creation of User 
            //=> id is automatically created inside database, doesn't need to be set here
            //=> isBanned is false at default, doesn't need to be set here
            //=> deletedAt should be null at creation, set date only after User deleted account
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getProfileImagePath());
            statement.setString(5, user.getDeletedAt());
            statement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            log.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't create user");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User '" + user.getName() + "' created");
    }

    //Search specific User by using id as filter
    @Override
    public User read(Long ID) {
        //SQL Statement to filter User where id equals parameter ID
        String readSQL = "Select * FROM users WHERE id = ?;";
        //User placeholder
        User user = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            
            ResultSet result = statement.executeQuery();
            //Create new Article Object using results from statement above
            while (result.next()) {
                //For every entry create new Article Object...
                user = new User(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("password"),
                        result.getString("role"),
                        result.getString("profileImagePath"),
                        result.getString("createdAt"),
                        result.getString("deletedAt"),
                        result.getBoolean("isBanned")
                );
            }
        }   
        catch (SQLException e) 
        {
            log.error(e.getMessage());
        }

        //Return filtered article
        if(user != null)
        {
            return user;
        } 
        else 
        {
            throw new NullPointerException("User not found!");
        }
    }

    //Update existing User based on id
    @Override
    public ResponseEntity<String> update(User user, Long userId) {

        //To change User:
        //=> User Object = Object containing changed settings
        //=> userId = id of to changing User
        //=> Fields id, profileImagePath and createdAt are unchangeable

        //SQL Statement to add new Article to database
        String updateSQL = "UPDATE users SET name = ?, password = ?, role = ?, deletedAt = ?, isBanned " +
                        "WHERE id = ?;";
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) 
        {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getDeletedAt());
            statement.setBoolean(5, user.getIsBanned());
            statement.setLong(6, userId);
            statement.executeUpdate();
        } 
        catch (SQLException e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't change user");
            log.error(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("User '" + user.getName() + "' changed");

    }

    //Delete existing User based on id
    @Override
    public ResponseEntity<String> delete(Long ID) {
        //SQL Statement to delete User
        String deleteSQL = "DELETE FROM users WHERE id = ? ";
        //execute statement
         try(PreparedStatement statement = connection.prepareStatement(deleteSQL)){
            statement.executeUpdate();
        } catch (SQLException e)
        {
            log.error(e.getMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        return  ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }
}
