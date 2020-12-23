package com.asset.appwork.orgchart;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.soap.Process;
import com.asset.appwork.platform.soap.Workflow;
import com.asset.appwork.platform.util.CordysUtil;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.util.ReflectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ModuleRouting {
    String config;
    Account account;
    String cordysUrl;

    public ModuleRouting(Account account, String cordysUrl, String config) throws AppworkException {
        this.config = config;
        this.account = account;
        this.cordysUrl= cordysUrl;
    }

    private <T> void calculateNextStep( T outputSchema) throws AppworkException {
        try {
            String nextStep = "";

            RoutingConfig routingConfig = generateRoutingConfig();

            // Condition On decision and code to get next step
            String[] currentStepId = {""};
            String[] codeSelected = {""};
             ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s)->{
                currentStepId[0] = (String) s;
             }).ifPresent("getCode", (s) -> {
                 codeSelected[0] = (String) s;
             });
//             outputSchema.getStepId();

            if(routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(codeSelected[0])){
                nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(codeSelected[0]);

                String nextPage = routingConfig.getSteps().get(nextStep).getPage();
                //TODO: Create Setter function in reflection class
                ((OutputSchema)outputSchema).setPage(nextPage);
            }else {
                nextStep = "break";
            }

            ((OutputSchema)outputSchema).setStepId(nextStep);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    public <T> void goToNext( T outputSchema) throws AppworkException {
        String[] currentStepId = {""};
        ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s)->{
            currentStepId[0] = (String) s;
        });
        calculateNextStep(outputSchema);
        if (currentStepId[0].equals("init")) initiateProcess(outputSchema);
        else completeWorkflow(outputSchema);

    }

    private RoutingConfig generateRoutingConfig() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(config);
        return objectMapper.convertValue(jsonNode, RoutingConfig.class);
    }


    private <T> void initiateProcess( T outputSchema) throws AppworkException {
        try {
            String[] params = {""};
            ReflectionUtil.of(outputSchema).ifPresent("getXML", (s)->{
                params[0] = (String) s;
            });
            String processInitiateMessage = new Process().initiate(params[0]);
            CordysUtil cordysUtil = new CordysUtil(cordysUrl);
            cordysUtil.sendRequest(account, processInitiateMessage);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    private <T> void completeWorkflow(T outputSchema) throws AppworkException {
        try {
            String[] data = {""};
            String[] taskId = {""};
            ReflectionUtil.of(outputSchema).ifPresent("getXMLWithNameSpace", (s)->{
                data[0] = (String) s;
            }).ifPresent("getTaskId", (s)->{
                taskId[0] = (String) s;
            });
            String completeWorkflowMessage = new Workflow().performTaskAction(taskId[0], "COMPLETE","", data[0]);
            CordysUtil cordysUtil = new CordysUtil(cordysUrl);
            cordysUtil.sendRequest(account, completeWorkflowMessage);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
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
    }
}
