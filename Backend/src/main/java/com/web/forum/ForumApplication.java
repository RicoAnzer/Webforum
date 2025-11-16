package com.web.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.web.forum.Configurations.PostgresProperties;
import com.web.forum.Database.DatabaseManager;

@SpringBootApplication
public class ForumApplication {

	private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public ForumApplication(PostgresProperties postgresProperties)
    {
        dbUrl = postgresProperties.getUrl();
        dbUser = postgresProperties.getUsername();
        dbPassword = postgresProperties.getPassword();
    }

	public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ForumApplication.class, args);
        DatabaseManager databaseManager = context.getBean(DatabaseManager.class);
        databaseManager.startDatabase(dbUrl, dbUser, dbPassword);
	}

}
