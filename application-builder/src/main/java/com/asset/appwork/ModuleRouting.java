package com.asset.appwork;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import javafx.util.Pair;
import lombok.Data;

import java.io.*;
import java.util.*;

public class ModuleRouting {
    String rootPath;
    String moduleName;

    public ModuleRouting(String rootPath, String moduleName) {
        this.rootPath = rootPath;
        this.moduleName = moduleName;
    }

    public String calculateNextStep(String stepId, String decision) throws AppworkException {
        try {

            String fileData = readFile(rootPath + "\\" + moduleName + ".json").toString();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(fileData);
            RoutingConfig routingConfig = objectMapper.convertValue(jsonNode, RoutingConfig.class);
            System.out.println(routingConfig);

            return "break";
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
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

    @Data
    static class RoutingConfig {
        Map<String,StepConfig> steps;
        String processName;
    }

    @Data
    static class StepConfig {
        boolean hasAutocomplete;
        String roleFilter, unitFilter, userFilter;
        String page;
        String condition;
//        List<Pair<String,String>> nextStep;
        //TODO Convert To Pair
        HashMap<String,String> nextStep;

    }

//    static class PairSerializer extends JsonSerializer<Pair> {
//
//        @Override
//        public void serialize(
//                Pair pair,
//                JsonGenerator jsonGenerator,
//                SerializerProvider serializerProvider) throws IOException {
//            jsonGenerator.writeStartArray(2);
//            jsonGenerator.writeObject(pair.getKey());
//            jsonGenerator.writeObject(pair.getValue());
//            jsonGenerator.writeEndArray();
//        }
//    }
//
//    static class PairDeserializer extends JsonDeserializer<Pair> {
//
//        @Override
//        public Pair deserialize(
//                JsonParser jsonParser,
//                DeserializationContext deserializationContext) throws IOException {
//            final Object[] array = jsonParser.readValueAs(Object[].class);
//            Pair pair = new Pair(array[0], array[1]);
//            return pair;
//        }
//    }
}
