package com.asset.appwork;

import java.io.*;

public class AppBuilder {
    static String rootPath;

    public AppBuilder(String rootPath) {
        AppBuilder.rootPath = rootPath;
    }


    public void run(String... args) {
        if (args.length == 0) return;
        String nameOfViewFolder = args[0].split("\\.")[0];

        String formPath = rootPath + File.separator + "config" + File.separator + nameOfViewFolder + ".conf";
        StringBuilder fileContent;
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
//            Pattern pattern = Pattern.compile("(?<=\\$path\\()(.*)(?=\\))");
//            Matcher matcher = pattern.matcher(fileContent);
//            while (matcher.find()) {
//                String path = matcher.group();
//                System.out.println(path);
//                String file = readFile(rootPath + File.separator + "views" + File.separator + path + ".json").toString();
//                fileContent = fileContent.replace(matcher.start() - 6, matcher.end() + 1, file);
//            }
            writeFile(rootPath + File.separator + "output" + File.separator + nameOfViewFolder + ".json", fileContent.toString());

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
