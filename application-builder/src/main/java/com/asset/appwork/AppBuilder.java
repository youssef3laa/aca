package com.asset.appwork;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;

import java.io.*;

public class AppBuilder {
    static String rootPath;

    public AppBuilder(String rootPath) {
        AppBuilder.rootPath = rootPath;
    }

    public StringBuilder retrieveFile(String fileName) throws AppworkException {
        String nameOfViewFolder = fileName;

        String formPath = rootPath + File.separator + "config" + File.separator + nameOfViewFolder + ".conf";
        StringBuilder fileContent = new StringBuilder();
        try {
            fileContent = readFile(formPath);
            while(fileContent.indexOf("$path(") > -1){
                int firstIndex = fileContent.indexOf("$path(")+6;
                int lastIndex = firstIndex + fileContent.substring(firstIndex).indexOf(")");
                String[] paths = fileContent.substring(firstIndex,lastIndex).split("\\\\");

                StringBuilder path = new StringBuilder();
                if(paths.length > 0){
//                    paths[0] = paths[0].replaceAll("\\\\", File.separator);

                    path = new StringBuilder(paths[0]);

                    for(int i =1 ; i < paths.length ; i++) {
//                        paths[i] = paths[i].replaceAll("\\\\", File.separator);
                        path.append(File.separator).append(paths[i]);
                    }
                }
                String file = readFile(rootPath+ File.separator + "views" + File.separator + path+ ".json").toString();
                fileContent.replace(firstIndex-6,lastIndex+1, file);
            }

//            writeFile(nameOfViewFolder, fileContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
            throw new AppworkException(ResponseCode.APP_BUILDER_FAILURE);
        }

        return fileContent;
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

    public void writeFile(String nameOfFile, String fileContent) throws IOException {
        String rootPath = this.rootPath + File.separator + "output" + File.separator + nameOfFile + ".json";
        try (FileWriter fr = new FileWriter(rootPath);
             BufferedWriter br = new BufferedWriter(fr)) {
            br.write(fileContent);
        }
    }
}
