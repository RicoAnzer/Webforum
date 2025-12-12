package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.web.forum.Configurations.PostgresProperties;
import com.web.forum.Database.DatabaseManager;
import com.web.forum.Entity.User;
import com.web.forum.Roles;

@Repository
public class UserDAO implements IUserDAO {

    //Logger
    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
    //Connection
    private final Connection connection;

    //Constructor
    public UserDAO(DatabaseManager databaseManager, PostgresProperties postgresProperties)
    {
        databaseManager.startDatabase(postgresProperties.getUrl(), postgresProperties.getUsername(), postgresProperties.getPassword());
        this.connection = databaseManager.connection;
    }

    //Save a new Article to database
    @Override
    public ResponseEntity<String> create(User user) {
        //SQL Statement to add new users to database
        String createSQL = "INSERT INTO users (name, password, role, profile_image_path, created_at, deleted_at, is_banned)" +
               "VALUES (?, ?, ?, ?, ?, ?, ?);";

        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(createSQL)) 
        {
            //At creation of User 
            //=> id is automatically created inside database, doesn't need to be set here
            //=> isBanned is false at default, doesn't need to be set here
            //=> deletedAt should be null at creation, set date only after User deleted account
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.setString(4, user.getProfileImagePath());
            statement.setString(5, user.getCreatedAt());
            statement.setString(6, user.getDeletedAt());
            statement.setBoolean(7, user.getIsBanned());
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
            statement.setLong(1, ID);
            ResultSet result = statement.executeQuery();
            //Create new Article Object using results from statement above
            while (result.next()) {
                //For every entry...
                //...check user role and...
                Roles userRole = null;
                switch (result.getString("role")) {
                    case "USER" -> userRole = Roles.USER;
                    case "MODERATOR" -> userRole = Roles.MODERATOR;
                }
                //...create new Article Object
                user = new User(

                        result.getString("name"),
                        result.getString("password"),
                        userRole,
                        result.getString("profile_image_path"),
                        result.getString("created_at"),
                        result.getString("deleted_at"),
                        result.getBoolean("is_Banned")
                );
            }
        }   
        catch (SQLException e) 
        {
            log.error(e.getMessage());
        }
        return user;
    }

    //Search specific User by using id as filter
    @Override
    public User readName(String username) {
        //SQL Statement to filter User where id equals parameter ID
        String readSQL = "Select * FROM users WHERE name = ?;";
        //User placeholder
        User user = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            //Create new Article Object using results from statement above
            while (result.next()) {
                //For every entry...
                //...check user role and...
                Roles userRole = null;
                switch (result.getString("role")) {
                    case "user" -> userRole = Roles.USER;
                    case "moderator" -> userRole = Roles.MODERATOR;
                }
                //...create new Article Object
                user = new User(
                        result.getString("name"),
                        result.getString("password"),
                        userRole,
                        result.getString("profile_image_path"),
                        result.getString("created_at"),
                        result.getString("deleted_at"),
                        result.getBoolean("is_Banned")
                );
            }
        }   
        catch (SQLException e) 
        {
            log.error(e.getMessage());
        }
        return user;
    }

    //Update existing User based on id
    @Override
    public ResponseEntity<String> update(User user, Long userId) {

        //To change User:
        //=> User Object = Object containing changed settings
        //=> userId = id of to changing User
        //=> Fields id, profileImagePath and createdAt are unchangeable

        //SQL Statement to add new Article to database
        String updateSQL = "UPDATE users SET name = ?, password = ?, role = ?, deleted_at = ?, is_banned " +
                        "WHERE id = ?;";
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) 
        {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
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
