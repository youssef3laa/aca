package com.asset.appwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by karim on 12/1/20.
 */
@SpringBootApplication
public class ApplicationBuilder {



    public static void main(String[] args) {

//        SpringApplication.run(ApplicationBuilder.class, args);

        /*
        page1.json - page1.conf
        page1.conf $path('files')
        read file(path) json
        write and replace in page1.json
         */


    }

}
