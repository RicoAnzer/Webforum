package com.web.forum;

import java.sql.Connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.web.forum.Configurations.PostgresProperties;
import com.web.forum.Database.DatabaseManager;

@SpringBootApplication
public class ForumApplication {

    public final Connection connection;

    //Constructor
    public ForumApplication(DatabaseManager databaseManager, PostgresProperties postgresProperties) {
        //Start an connect to database
        databaseManager.startDatabase(postgresProperties.getUrl(), postgresProperties.getUsername(), postgresProperties.getPassword());
        this.connection = databaseManager.connection;
    }

    public static void main(String[] args) {
        //Start Application
        SpringApplication.run(ForumApplication.class, args);
    }

}
