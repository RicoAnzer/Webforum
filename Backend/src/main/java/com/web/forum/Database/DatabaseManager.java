package com.web.forum.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {

    //Logger
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    //Statement to execute SQL commands
    public Statement statement;
    //Connection
    public Connection connection;

    //Starting database
    public void startDatabase(String dbUrl, String dbUser, String dbPassword){
        try{
            //Load PostgreSQL JDB driver
            Class.forName("org.postgresql.Driver");
            //Establish database connection
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            //Create statement
            statement = connection.createStatement();

            /**
             * Initialize all tables
             * => A topic contains multple threads
             * => Each thread contains multiple posts
             * => Each post is written by an user
             */
             //Initialize table for topics
            String createTopicSQL = "CREATE TABLE IF NOT EXISTS topics (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VarChar(100) " +
                    ")";
            //Initialize table for threads
            String createThreadSQL = "CREATE TABLE IF NOT EXISTS threads (" +
                    "id SERIAL PRIMARY KEY," +
                    "topic_id INT, " +
                    "name VarChar(100), " +
                    "CONSTRAINT fk_topic_id FOREIGN KEY (topic_id)" +
                    "REFERENCES topics(id) ON DELETE CASCADE" +
                    ")";
            //Initialize table for users
            String createUserSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VarChar(50), " +
                    "password VarChar(50), " +
                    "role VarChar(15), " +
                    "profile_image_path VarChar(100), " +
                    "created_at VarChar(10), " +
                    "deleted_at VarChar(10), " +
                    "is_banned Boolean" +
                    ")";
            //Initialize table for posts
            String createPostSQL = "CREATE TABLE IF NOT EXISTS posts (" +
                    "id SERIAL PRIMARY KEY," +
                    "user_id INT, " +
                    "thread_id INT, " +
                    "content JSONB, " +
                    "CONSTRAINT fk_user_id FOREIGN KEY (user_id)" +
                    "REFERENCES users(id) ON DELETE SET NULL," +
                    "CONSTRAINT fk_thread_id FOREIGN KEY (thread_id)" +
                    "REFERENCES threads(id) ON DELETE CASCADE" +
                    ")";
            
            statement.execute(createTopicSQL);
            statement.execute(createThreadSQL);
            statement.execute(createUserSQL);
            statement.execute(createPostSQL);  
        }
        catch(ClassNotFoundException | SQLException e){
            log.error(e.getMessage());
        }
    }
    //Close database connection
    public void closeDatabase() throws SQLException {
        connection.close();
    }

}