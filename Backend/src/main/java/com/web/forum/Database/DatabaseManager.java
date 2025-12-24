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

    public Statement statement;
    public Connection connection;
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    //Starting database
    public void startDatabase(String dbUrl, String dbUser, String dbPassword) {
        try {
            //Load PostgreSQL JDB driver
            Class.forName("org.postgresql.Driver");
            //Establish database connection
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            //Create statement
            statement = connection.createStatement();

            /**
             * Initialize all tables => A topic contains multple threads => Each
             * thread contains multiple posts => Each post is written by an user
             * => An user has one or multiple roles
             */
            //Initialize table for topics
            String createTopicSQL = "CREATE TABLE IF NOT EXISTS topics ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VarChar(100) "
                    + ")";
            //Initialize table for threads
            String createThreadSQL = "CREATE TABLE IF NOT EXISTS threads ("
                    + "id SERIAL PRIMARY KEY,"
                    + "topic_id INT, "
                    + "name VarChar(100), "
                    + "CONSTRAINT fk_topic_id FOREIGN KEY (topic_id)"
                    + "REFERENCES topics(id) ON DELETE CASCADE"
                    + ")";
            //Initialize table for roles
            String createRoleSQL = "CREATE TABLE IF NOT EXISTS roles ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VarChar(15)"
                    + ")";
            //Fill table roles with user, moderator and admin roles
            String fillRolesSQL = String.format("INSERT INTO roles(id, name) "
                    + "VALUES (%d, '%s'), (%d, '%s'), (%d, '%s') ON CONFLICT (id) DO NOTHING;",
                    1, "USER", 2, "MODERATOR", 3, "ADMIN");
            //Initialize table for users
            String createUserSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VarChar(50), "
                    + "password Text, "
                    + "profile_image_path VarChar(100), "
                    + "created_at VarChar(10), "
                    + "deleted_at VarChar(10), "
                    + "is_banned Boolean"
                    + ")";
            //Initialize table for user roles 
            //Example:
            //Id User = 1
            //Id Patrick = 1
            //User Role (1,1) => Patrick has role User
            String createUserRoleSQL = "CREATE TABLE IF NOT EXISTS user_roles ("
                    + "user_id INT NOT NULL,"
                    + "role_id INT NOT NULL,"
                    + "CONSTRAINT fk_user_id FOREIGN KEY (user_id)"
                    + "REFERENCES users(id) ON DELETE CASCADE,"
                    + "CONSTRAINT fk_role_id FOREIGN KEY (role_id)"
                    + "REFERENCES roles(id) ON DELETE CASCADE"
                    + ")";
            //Initialize table for posts
            String createPostSQL = "CREATE TABLE IF NOT EXISTS posts ("
                    + "id SERIAL PRIMARY KEY,"
                    + "user_id INT, "
                    + "thread_id INT, "
                    + "content JSONB, "
                    + "CONSTRAINT fk_user_id FOREIGN KEY (user_id)"
                    + "REFERENCES users(id) ON DELETE SET NULL,"
                    + "CONSTRAINT fk_thread_id FOREIGN KEY (thread_id)"
                    + "REFERENCES threads(id) ON DELETE CASCADE"
                    + ")";

            //Create tables using statements from above
            statement.execute(createTopicSQL);
            statement.execute(createThreadSQL);
            statement.execute(createRoleSQL);
            statement.execute(fillRolesSQL);
            statement.execute(createUserSQL);
            statement.execute(createUserRoleSQL);
            statement.execute(createPostSQL);
        } catch (ClassNotFoundException | SQLException e) {
            log.error(e.getMessage());
        }
    }

    //Close database connection
    public void closeDatabase() throws SQLException {
        connection.close();
    }

}
