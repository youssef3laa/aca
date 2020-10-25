package com.asset.appwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by karim on 10/19/20.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class WebAppApplication  {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);

    }
}
