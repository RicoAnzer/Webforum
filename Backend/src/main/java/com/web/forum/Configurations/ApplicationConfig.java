package com.web.forum.Configurations;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//Configuration class for PostgresProperties
//Required for PostgresProperties class which accesses values in application.properties to connect to database
@Configuration
@EnableConfigurationProperties(PostgresProperties.class)
public class ApplicationConfig {
}
