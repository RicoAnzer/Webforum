package com.web.forum.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.forum.DAO.Interfaces.IPostDAO;
import com.web.forum.Entity.Post;
import com.web.forum.ForumApplication;

@Repository
public class PostDAO implements IPostDAO {

    private static final Logger log = LoggerFactory.getLogger(PostDAO.class);
    private final Connection connection;
    private final ObjectMapper objectMapper;

    // Constructor
    public PostDAO(ForumApplication forumApplication, ObjectMapper objectMapper) {
        // Connect to database;
        this.connection = forumApplication.connection;
        this.objectMapper = objectMapper;
    }

    // Create and save new Post
    @Override
    public Post create(Post post) {
        // SQL Statement to add new Posts to database
        String createSQL = "INSERT INTO posts (user_id, thread_slug, content, created_at, updated_at, deleted)"
                + "VALUES (?, ?, ?::jsonb, ?, ?, ?)"
                + "RETURNING id, user_id, thread_slug, content, created_at, updated_at, deleted;";
        Post generatedPost = null;
        // Format createdAt to "dd-MM-YYYY"
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        // Execute statement
        try (PreparedStatement statement = connection.prepareStatement(createSQL)) {
            // At creation of Post
            // => id is automatically created inside database, id can be null at this stage
            statement.setLong(1, post.getUserId());
            statement.setString(2, post.getThread_slug());
            statement.setString(3, post.getContent().toString());
            statement.setObject(4, LocalDateTime.now());
            statement.setObject(5, null);
            statement.setBoolean(6, post.getDeleted());
            statement.execute();
            
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                // Retrieve and convert jsonb to JsonNode
                String contentJson = result.getString("content");
                JsonNode content = null;
                try {
                    content = objectMapper.readTree(contentJson);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
                // Check if updatedAt was set or not => return null if not set
                LocalDateTime updatedAt = result.getObject("updated_at", LocalDateTime.class);
                String formattedUpdatedAt = (updatedAt != null) ? updatedAt.format(dateTimeFormatter) : null;
                generatedPost = new Post(
                        result.getLong("id"),
                        result.getLong("user_id"),
                        result.getString("thread_slug"),
                        content,
                        result.getObject("created_at", LocalDateTime.class).format(dateTimeFormatter),
                        formattedUpdatedAt,
                        result.getBoolean("deleted"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return generatedPost;
    }

    // Find specific Post based on Id
    @Override
    public Post readId(Long Id) {
        // SQL Statement to return posts of specific ID
        String readSQL = "Select * FROM posts WHERE id = ?;";
        Post post = null;
        // Format createdAt to "dd-MM-YYYY"
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        // Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setLong(1, Id);
            ResultSet result = statement.executeQuery();
            // Create new Posts Object using results from statement above
            while (result.next()) {
                // Retrieve and convert jsonb to JsonNode
                String contentJson = result.getString("content");
                JsonNode content = null;
                try {
                    content = objectMapper.readTree(contentJson);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
                // Check if updatedAt was set or not => return null if not set
                LocalDateTime updatedAt = result.getObject("updated_at", LocalDateTime.class);
                String formattedUpdatedAt = (updatedAt != null) ? updatedAt.format(dateTimeFormatter) : null;
                post = new Post(
                        result.getLong("id"),
                        result.getLong("user_id"),
                        result.getString("thread_slug"),
                        content,
                        result.getObject("created_at", LocalDateTime.class).format(dateTimeFormatter),
                        formattedUpdatedAt,
                        result.getBoolean("deleted"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return post;
    }

    // Search and return Posts related to a sepcific Thread
    @Override
    public List<Post> readAll(String threadSlug) {
        // SQL Statement to return all Posts of a specific Thread
        String readSQL = "Select * FROM posts WHERE thread_slug = ?;";
        // Post list placeholder
        List<Post> posts = new ArrayList<>();
        // Format createdAt to "dd-MM-YYYY"
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        // Execute statement
        try (PreparedStatement statement = connection.prepareStatement(readSQL)) {
            statement.setString(1, threadSlug);
            ResultSet result = statement.executeQuery();
            // Create new Posts Objects using results from statement above
            while (result.next()) {
                // Retrieve and convert jsonb to JsonNode
                String contentJson = result.getString("content");
                JsonNode content = null;
                try {
                    content = objectMapper.readTree(contentJson);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
                // Check if updatedAt was set or not => return null if not set
                LocalDateTime updatedAt = result.getObject("updated_at", LocalDateTime.class);
                String formattedUpdatedAt = (updatedAt != null) ? updatedAt.format(dateTimeFormatter) : null;
                Post post = new Post(
                        result.getLong("id"),
                        result.getLong("user_id"),
                        result.getString("thread_slug"),
                        content,
                        result.getObject("created_at", LocalDateTime.class).format(dateTimeFormatter),
                        formattedUpdatedAt,
                        result.getBoolean("deleted"));

                posts.add(post);

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return posts;
    }

    // Update existing Post
    @Override
    public Post update(Post updatedPost) {
        // To change Post:
        // => updatedPost Object = Object containing changed settings
        // => Fields id, user_id, thread_slug and createdAt are unchangeable
        String updateSQL = "UPDATE posts SET content = ?, created_at = ?, updated_at = ?, deleted = ? "
                + "WHERE id = ?;";
        // Format createdAt to "dd-MM-YYYY"
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        // Execute statement
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            // Check if updatedAt is set or not => return null if not set
            LocalDateTime updatedAt = (updatedPost.getUpdated_at() != null && !updatedPost.getUpdated_at().isBlank()) ? LocalDateTime.parse(updatedPost.getUpdated_at()) : null;
            statement.setObject(1, updatedPost.getContent().toString(), Types.OTHER);
            statement.setObject(2, LocalDateTime.parse(updatedPost.getCreated_at(), dateTimeFormatter));
            statement.setObject(3, updatedAt);
            statement.setBoolean(4, updatedPost.getDeleted());
            statement.setLong(5, updatedPost.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return updatedPost;
    }

    // Delete existing Post
    @Override
    public String delete(Long Id) {
        // SQL Statement to delete Post
        String deleteSQL = "DELETE FROM posts WHERE id = ?;";
        // execute statement
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setLong(1, Id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return "Deleted Post Number '" + Id + "'";
    }
}
