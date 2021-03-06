package com.asset.appwork;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppBuilder {
    static String rootPath;

    public AppBuilder(String rootPath){
        AppBuilder.rootPath = rootPath;
    }


    public void run(String... args) {
        if (args.length == 0) return;
        String nameOfViewFolder = args[0].split("\\.")[0];

        String formPath = rootPath + "\\config\\" +  nameOfViewFolder + ".conf";
        StringBuilder fileContent;
        try {
            fileContent = readFile(formPath);
            Pattern pattern = Pattern.compile("(?<=\\$path\\()(.*)(?=\\))");
            Matcher matcher = pattern.matcher(fileContent);
            while (matcher.find()) {
                String path = matcher.group();
                System.out.println(path);
                String file = readFile(rootPath + "\\views\\" + path+".json").toString();
                fileContent = fileContent.replace(matcher.start() - 6, matcher.end() + 1, file);

            }
            writeFile(rootPath + "\\output\\"  + nameOfViewFolder + ".json", fileContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private StringBuilder readFile(String rootPath) throws IOException {
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

    private void writeFile(String rootPath, String fileContent) throws IOException {
        try (FileWriter fr = new FileWriter(rootPath);
             BufferedWriter br = new BufferedWriter(fr)) {
            br.write(fileContent);
        }
    }
}
