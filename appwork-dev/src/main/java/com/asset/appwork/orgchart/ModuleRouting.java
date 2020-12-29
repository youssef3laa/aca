package com.asset.appwork.orgchart;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.ApprovalHistory;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.platform.soap.Process;
import com.asset.appwork.platform.soap.Workflow;
import com.asset.appwork.platform.util.CordysUtil;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.util.ReflectionUtil;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

import java.util.*;

public class ModuleRouting {
    String config;
    Account account;
    String cordysUrl;
    String restAPIBaseUrl;

    public ModuleRouting(Account account,  String cordysUrl, String restAPIBaseUrl, String config) throws AppworkException {
        this.config = config;
        this.account = account;
        this.cordysUrl= cordysUrl;
        this.restAPIBaseUrl = restAPIBaseUrl;
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

    private RoutingConfig generateRoutingConfig() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(config);
        return objectMapper.convertValue(jsonNode, RoutingConfig.class);
    }

    public <T> String goToNext( T outputSchema) throws AppworkException {
        String[] currentStepId = {""};
        ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s)->{
            currentStepId[0] = (String) s;
        });
        calculateNextStep(outputSchema);
        if (currentStepId[0].equals("init"))
            return initiateProcess(outputSchema);
        else
            return completeWorkflow(outputSchema);
    }

    private <T> String initiateProcess( T outputSchema) throws AppworkException {
        String response = null;
        try {
            String[] params = {""};
            ReflectionUtil.of(outputSchema).ifPresent("getXML", (s)->{
                params[0] = (String) s;
            });
            String processInitiateMessage = new Process().initiate(params[0]);
            CordysUtil cordysUtil = new CordysUtil(cordysUrl);
            response =  cordysUtil.sendRequest(account, processInitiateMessage);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
        return response;
    }

    private <T> String completeWorkflow(T outputSchema) throws AppworkException {
        String response = null;
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
            response= cordysUtil.sendRequest(account, completeWorkflowMessage);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
        return response;
    }

    private <T> void calculateNextStep( T outputSchema) throws AppworkException {
        //TODO: Create Setter function in reflection class
        try {
            String nextStep = "";
            String nextPage = "";

            RoutingConfig routingConfig = generateRoutingConfig();

            String[] currentStepId = {""};
            String[] codeSelected = {""};

            ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s)->{
                currentStepId[0] = (String) s;
            }).ifPresent("getCode", (s) -> {
                 codeSelected[0] = (String) s;
            });

            if(codeSelected[0].equals("approve")){
                // If assignee code in next steps
                // Else go to parent step
                String assigneeCode = calculateNextAssignee(outputSchema);

                if(routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(assigneeCode)){
                    nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(assigneeCode);
                } else if(routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(codeSelected[0])){
                    nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(codeSelected[0]);
                }

            } else if(codeSelected[0].equals("requestModification")){
                // If code selected and is in nextSteps
                // go to specific step
                // Else get previous step from approval history
                if(routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(codeSelected[0])){
                    nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(codeSelected[0]);
                }else {
                    nextStep = calculateFromApprovalHistory(outputSchema);
                }

            } else if(routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(codeSelected[0])){
                nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(codeSelected[0]);

            } else if(codeSelected[0].equals("reject")){
                nextStep = "break";
            }

            if(nextStep.isEmpty()){
                nextStep = "break";
            }

            if(!nextStep.equals("break")){
                nextPage = routingConfig.getSteps().get(nextStep).getPage();
                ((OutputSchema)outputSchema).setPage(nextPage);
            }

            ((OutputSchema)outputSchema).setStepId(nextStep);
        }catch (Exception e){
            e.printStackTrace();
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    private <T> String calculateNextAssignee(T outputSchema){
        ((OutputSchema)outputSchema).setAssignedCN("cn=Aly@aw.aca,cn=organizational users,o=aca,cn=cordys,cn=defaultInst,o=appworks-aca.local");
        String assigneeCode = "HIROLE";
        return assigneeCode;
    }

    private <T> String calculateFromApprovalHistory(T outputSchema) throws JsonProcessingException {
        String[] processId = {""}; //"000C292D-1114-A1EB-9217-3FF5C7814E9C"
        ReflectionUtil.of(outputSchema).ifPresent("getProcessId", (s)->{
            processId[0] = (String) s;
        });

        Entity entity = new Entity(account, restAPIBaseUrl, "ACA_Entity_approval_history");
        String list = entity.readList("ProcessHistory",
                "{\n" +
                        "  \"top\": 1,\n" +
                        "  \"parameters\": {\n" +
                        "    \"Properties.processId\": {\n" +
                        "      \"name\": \"\",\n" +
                        "      \"comparison\": {\n" +
                        "        \"value\": \""+ processId[0] +"\",\n" +
                        "        \"operator\": \"EQ\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"orderBy\": [{\n" +
                        "    \"name\": \"Identity.Id\",\n" +
                        "    \"sortType\": \"Desc\"\n" +
                        "  }],\n" +
                        "  \"select\": []\n" +
                        "}");

        list = SystemUtil.readJSONObject(list, "_embedded");
        List<ApprovalHistory> histories = SystemUtil.readJSONArray(list, "ProcessHistory", ApprovalHistory.class);
        if (histories.size() > 0) {
            ((OutputSchema)outputSchema).setAssignedCN(histories.get(0).getProperties().getUserCN());
            return histories.get(0).getProperties().getStepId();
        }

        return "break";
    }
}
