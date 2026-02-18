package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.Interfaces.IThreadDAO;
import com.web.forum.Entity.Thread;
import com.web.forum.ForumApplication;

@Repository
public class ThreadDAO implements IThreadDAO {

    private static final Logger log = LoggerFactory.getLogger(ThreadDAO.class);
    private final Connection connection;

    //Constructor
    public ThreadDAO(ForumApplication forumApplication) {
        //Connect to database;
        this.connection = forumApplication.connection;
    }

    //Save a new Thread to database
    @Override
    public Thread create(String name, Long topicId) {
        //SQL Statement to add new Threads to database
        String createSQL = "INSERT INTO threads (topic_id, name, slug)"
                + "VALUES (?, ?, ?)"
                + "RETURNING id, topic_id, name, slug;";

        //Generate slug
        //Remove all non ASCII Symbols like "@"
        Pattern NON_ASCII = Pattern.compile("[^\\p{ASCII}]");
        //Remove all characters but letters (a-z) and numbers
        Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]+");
        //Create slug out of name
        //=> "This is a test name" to "this-is-a-test-name"
        //1. Remove all accents (ä to a), 2. Remove all non ASCII Symbols like "@"
        //3. All letters to lowerCase, 4. All characters but a-z and 0-9 to "-"
        //5. Delete leading and ending "-"
        String slug = Normalizer.normalize(name, Normalizer.Form.NFKD);
        slug = NON_ASCII.matcher(slug).replaceAll("");
        slug = NON_ALPHANUMERIC.matcher(slug.toLowerCase())
                                      .replaceAll("-")
                                      .replaceAll("^-|-$", "");
        Thread generatedThread = null;

        try (PreparedStatement statement = connection.prepareStatement(createSQL)) {
            //At creation of Thread 
            //=> id is automatically created inside database, doesn't need to be set here
            statement.setLong(1, topicId);
            statement.setString(2, name);
            statement.setString(3, slug);
            statement.execute();

            //Return newly generated Thread
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                generatedThread = new Thread(
                        result.getLong("id"),
                        result.getLong("topic_id"),
                        result.getString("name"),
                        result.getString("slug")
                );
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return generatedThread;
    }

    //Search and return Threads based on ID
    @Override
    public Thread readByName(String name) {
        //SQL Statement to return Thread of specific ID
        String readSQL = "Select * FROM threads WHERE name = ?;";
        //Thread placeholder
        Thread thread = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            //Create new Thread Object using results from statement above
            while (result.next()) {
                //For every entry...
                //...create new Thread Object...
                thread = new Thread(
                        result.getLong("id"),
                        result.getLong("topic_id"),
                        result.getString("name"),
                        result.getString("slug")
                );

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return thread;
    }

    //Search and return Threads related to a sepcific Topic
    @Override
    public List<Thread> readAll(Long topicId) {
        //SQL Statement to return all Threads of a specific Topic
        String readSQL = "Select * FROM threads WHERE topic_id = ?;";
        //Thread placeholder
        List<Thread> threads = new ArrayList<>();
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setLong(1, topicId);
            ResultSet result = statement.executeQuery();
            //Create new Threads Objects using results from statement above
            while (result.next()) {
                //For every entry...
                //...create new Thread Object...
                Thread thread = new Thread(
                        result.getLong("id"),
                        result.getLong("topic_id"),
                        result.getString("name"),
                        result.getString("slug")
                );
                threads.add(thread);

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return threads;
    }

    //Delete existing Thread based on name
    @Override
    public String delete(String name) {
        //SQL Statement to delete Topic
        String deleteSQL = "DELETE FROM threads WHERE name = ?;";
        //execute statement
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return "Thread deleted";
    }
}
