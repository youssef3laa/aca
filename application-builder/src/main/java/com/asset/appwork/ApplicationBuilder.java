package com.asset.appwork;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by karim on 12/1/20.
 */
@SpringBootApplication
public class ApplicationBuilder implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication.run(ApplicationBuilder.class, args);

        /*
        page1.json - page1.conf
        page1.conf $path('files')
        read file(path) json
        write and replace in page1.json
         */


    }

    @Override
    public void run(String... args) {
        if (args.length == 0) return;
        String nameOfViewFolder = args[0].split("\\.")[0];
        String rootStr = "form-config";
        String formPath = rootStr + "\\" + nameOfViewFolder + "\\" + args[0];
        StringBuilder fileContent;
        try {
            fileContent = readFile(formPath);
            Pattern pattern = Pattern.compile("(?<=\\$path\\()(.*)(?=\\))");
            Matcher matcher = pattern.matcher(fileContent);
            while (matcher.find()) {
                String path = matcher.group();
                System.out.println(path);
                String file = readFile(rootStr + "\\" + path).toString();
                fileContent = fileContent.replace(matcher.start() - 6, matcher.end() + 1, file);

            }
            writeFile(rootStr + "\\" + nameOfViewFolder + "\\" + nameOfViewFolder + ".json", fileContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public StringBuilder readFile(String rootPath) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        String line;
        try (FileReader fr = new FileReader(rootPath);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                fileContent.append(line);
                fileContent.append(System.lineSeparator());
            }

        }
        return fileContent;
    }

    public void writeFile(String rootPath, String fileContent) throws IOException {
        try (FileWriter fr = new FileWriter(rootPath);
             BufferedWriter br = new BufferedWriter(fr)) {
            br.write(fileContent);
        }
    }
}
