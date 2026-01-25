package com.web.forum.Configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

//Accesses values in application.properties to connect to database
@ConfigurationProperties(prefix = "spring.datasource")
public class PostgresProperties{

    //Initializes postgres database values
    private String url;
    private String username;
    private String password;

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

    //Setter
    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
