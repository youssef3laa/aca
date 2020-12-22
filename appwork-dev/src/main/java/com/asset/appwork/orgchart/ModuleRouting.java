package com.asset.appwork.orgchart;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.soap.Process;
import com.asset.appwork.platform.util.CordysUtil;
import com.asset.appwork.schema.OutputSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

import java.util.*;

public class ModuleRouting {
    String config;
    Account account;
    String cordysUrl;

    public ModuleRouting(Account account, String cordysUrl, String config) throws AppworkException {
        this.config = config;
        this.account = account;
        this.cordysUrl= cordysUrl;
    }

    private void calculateNextStep( OutputSchema outputSchema) throws AppworkException {
        try {
            String nextStep = "";

            RoutingConfig routingConfig = generateRoutingConfig();

            // Condition On decision and code to get next step
            String currentStepId = outputSchema.getStepId();
            String codeSelected = outputSchema.getCode();
            if(routingConfig.getSteps().get(currentStepId).getNextStep().containsKey(codeSelected)){
                nextStep = routingConfig.getSteps().get(currentStepId).getNextStep().get(codeSelected);
            }else {
                nextStep = "break";
            }

            outputSchema.setStepId(nextStep);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    public void goToNext( OutputSchema outputSchema) throws AppworkException {
        calculateNextStep( outputSchema);
        if (outputSchema.getStepId().equals("init")) initiateProcess(outputSchema);
        else completeWorkflow();

    }

    private RoutingConfig generateRoutingConfig() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(config);
        return objectMapper.convertValue(jsonNode, RoutingConfig.class);
    }


    private void initiateProcess( OutputSchema outputSchema) throws AppworkException {
        try {
            String params = outputSchema.getXML();
            String processInitiateMessage = new Process().initiate(params);
            CordysUtil cordysUtil = new CordysUtil(cordysUrl);
            cordysUtil.sendRequest(account, processInitiateMessage);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    private void completeWorkflow() {

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
        HashMap<String,String> nextStep;
        //TODO Convert To Pair
        //List<Pair<String,String>> nextStep;

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
