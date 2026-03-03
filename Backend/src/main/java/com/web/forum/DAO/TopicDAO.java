package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.Interfaces.ITopicDAO;
import com.web.forum.Entity.Topic;
import com.web.forum.ForumApplication;

@Repository
public class TopicDAO implements ITopicDAO {

    private static final Logger log = LoggerFactory.getLogger(TopicDAO.class);
    private final Connection connection;

    //Constructor
    public TopicDAO(ForumApplication forumApplication) {
        //Connect to database;
        this.connection = forumApplication.connection;
    }

    //Save a new Topic to database
    @Override
    public Topic create(Topic topic) {
        //SQL Statement to add new Topics to database
        String createSQL = "INSERT INTO topics (name, slug)"
                + "VALUES (?, ?)"
                + "RETURNING id, name, slug;";

        Topic generatedTopic = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(createSQL)) {
            //At creation of Topic 
            //=> id is automatically created inside database, doesn't need to be set here
            statement.setString(1, topic.getName());
            statement.setString(2, topic.getSlug());
            statement.execute();

            //Return newly generated Topic
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                generatedTopic = new Topic(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("slug")
                );
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return generatedTopic;
    }

    //Search and return Topics based on ID
    @Override
    public Topic readbyID(Long ID) {
        //SQL Statement to return Topic of a specific ID
        String readSQL = "Select * FROM topics WHERE id = ?;";
        //Topic placeholder
        Topic topic = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setLong(1, ID);
            ResultSet result = statement.executeQuery();
            //Create new Topic Object using results from statement above
            while (result.next()) {
                //For every entry...
                //...create new Topic Object...
                topic = new Topic(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("slug")
                );

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return topic;
    }

    //Find and return Topic in database based on name
    @Override
    public Topic readbyName(String name) {
        //SQL Statement to return all Topics
        String readSQL = "Select * FROM topics WHERE name = ?;";
        //Topic placeholder
        Topic topic = null;
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            //Create new Topic Object using results from statement above
            while (result.next()) {
                //For every entry...
                //...create new Topic Object...
                topic = new Topic(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("slug")
                );

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return topic;
    }

    //Search and return all Topics
    @Override
    public List<Topic> readAll() {
        //SQL Statement to return all Topics
        String readSQL = "Select * FROM topics;";
        //Topic placeholder
        List<Topic> topics = new ArrayList<>();
        //Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            ResultSet result = statement.executeQuery();
            //Create new Topic Objects using results from statement above
            while (result.next()) {
                //For every entry...
                //...create new Topic Object...
                Topic topic = new Topic(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("slug")
                );
                topics.add(topic);

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return topics;
    }

    //Delete existing Topic based on name
    @Override
    public String delete(String name) {
        //SQL Statement to delete Topic
        String deleteSQL = "DELETE FROM topics WHERE name = ?;";
        //execute statement
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return "Topic deleted";
    }
}
