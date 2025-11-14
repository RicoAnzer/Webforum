package com.web.forum.Configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

//Accesses values in application.properties to connect to database
//Two databases, production and testing
@ConfigurationProperties(prefix = "spring.datasource")
public class PostgresProperties{

    //Initializes postgres database values
    private final String url;
    private final String username;
    private final String password;

    //Constructor
    public PostgresProperties(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    //Getter
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
