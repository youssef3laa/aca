package com.asset.appwork.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * Created by karim on 10/19/20.
 */
@Configuration
public class AppConfig {
    @Autowired
    private Environment env;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    private final String DEFAULT_SCHEMA = "";

    @Bean
    public void printActiveProfile(){
        System.out.println("Currently active profile access from SpEL - " + activeProfile);
    }

    @Bean
    public void getActiveProfiles(){
        for (String profilename : env.getActiveProfiles()){
            System.out.println("Currently active profile access from env - " + profilename);
        }
    }


    @Profile("dev")
    @Bean
    public void dev(){
        System.out.println("Currently active profile access from property fie - " +env.getProperty("spring.datasource.username"));
        getActiveProfiles();
    }

    @Profile("test")
    @Bean
    public void test(){
        System.out.println("Currently active profile access from property fie - " +env.getProperty("spring.datasource.username"));
        getActiveProfiles();
    }

    @Profile("prod")
    @Bean
    public void prod(){
        System.out.println("Currently active profile access from property fie - " +env.getProperty("spring.datasource.username"));
        getActiveProfiles();
    }


}
