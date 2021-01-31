package com.asset.appwork.orgchart;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.Router;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.platform.soap.Process;
import com.asset.appwork.platform.soap.Workflow;
import com.asset.appwork.platform.util.CordysUtil;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.util.ReflectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ModuleRouting {
    private final String breakString = "end";
    private final String rejectString = "reject";
    private final String approveString = "approve";
    private final String redirectString = "redirect";
    private final String commentString = "comment";
    private final String parallelString = "multiple";
    private final String requestModificationString = "requestModification";

    String config;
    Account account;
    String cordysUrl;
    ApprovalHistoryRepository approvalHistoryRepository;
    OrgChartService orgChartService;

    public ModuleRouting(Account account, String cordysUrl, String config, ApprovalHistoryRepository approvalHistoryRepository, OrgChartService orgChartService) {
        this.config = config;
        this.account = account;
        this.cordysUrl = cordysUrl;
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.orgChartService = orgChartService;
    }

    @Data
    static class RoutingConfig {
        Map<String, StepConfig> steps;
        String processName = "";
    }

    @Data
    static class StepConfig<T> {
        Boolean addApproval = true;
        Router router = new Router();
        String page = "" ;
        String subBP = "";
        HashMap<String, String> nextStep = new HashMap<>();
        HashMap<String, T> extraData = new HashMap<>();
    }

    private RoutingConfig generateRoutingConfig() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(config);
        return objectMapper.convertValue(jsonNode, RoutingConfig.class);
    }

    public <T> String goToNext(T outputSchema) throws AppworkException {
        String[] currentStepId = {""};
        ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s) -> {
            currentStepId[0] = (String) s;
        });
        calculateNextStep(outputSchema);
        if (currentStepId[0].equals("init"))
            return initiateProcess(outputSchema);
        else
            return completeWorkflow(outputSchema);
    }

    private <T> String initiateProcess(T outputSchema) throws AppworkException {
        String response;
        try {
            String[] params = {""};
            ReflectionUtil.of(outputSchema).ifPresent("getXML", (s) -> {
                params[0] = (String) s;
            });
            String processInitiateMessage = new Process().initiate(params[0]);
            CordysUtil cordysUtil = new CordysUtil(cordysUrl);
            response = cordysUtil.sendRequest(account, processInitiateMessage);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ModuleRouting: "+ e.getMessage());
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
        return response;
    }

    private <T> String completeWorkflow(T outputSchema) throws AppworkException {
        String response;
        try {
            String[] data = {""};
            String[] taskId = {""};
            ReflectionUtil.of(outputSchema).ifPresent("getXMLWithNameSpace", (s) -> {
                data[0] = (String) s;
            }).ifPresent("getTaskId", (s) -> {
                taskId[0] = (String) s;
            });
            String completeWorkflowMessage = new Workflow().performTaskAction(taskId[0], "COMPLETE", "", data[0]);
            CordysUtil cordysUtil = new CordysUtil(cordysUrl);
            response = cordysUtil.sendRequest(account, completeWorkflowMessage);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ModuleRouting: "+ e.getMessage());
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
        return response;
    }

    private <T> void calculateNextStep(T outputSchema) throws AppworkException {
        //TODO: Create Setter function in reflection class
        try {
            String nextStep = "", nextPage = "", nextSubBP = "";
            Router nextRouter = new Router();

            RoutingConfig routingConfig = generateRoutingConfig();

            String[] currentStepId = {""}, codeSelected = {""}, decision = {""}, parentHistoryId = {""} , receiverType = {""};

            ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s) -> {
                currentStepId[0] = (String) s;
            }).ifPresent("getCode", (s) -> {
                codeSelected[0] = (String) s;
            }).ifPresent("getDecision", (s) -> {
                decision[0] = (String) s;
            }).ifPresent("getParentHistoryId", (s) -> {
                parentHistoryId[0] = (String) s;
            }).ifPresent("getReceiverType", (s) -> {
                receiverType[0] = (String) s;
            });

            ((OutputSchema) outputSchema).setParentHistoryId(null);

            if(codeSelected[0].isEmpty() && decision[0].isEmpty()){
                throw new AppworkException(ResponseCode.MODULE_ROUTING_INPUTS_ERROR);
            }

            if(receiverType[0].equals("multiple")){
                nextStep = getIdFromNextSteps(routingConfig, currentStepId[0], parallelString);
            }

            if(nextStep.isEmpty()) {
                decision[0] = getDecisionString(decision[0]);

                switch (decision[0]) {
                    case approveString:
                        nextStep = handleApprovalCase(routingConfig, currentStepId[0], codeSelected[0]);
                        break;
                    case redirectString:
                        nextStep = handleRedirectCase(routingConfig, currentStepId[0], codeSelected[0]);
                        break;
                    case requestModificationString:
                        nextStep = handleRequestModificationCase(outputSchema, routingConfig, currentStepId[0], parentHistoryId[0]);
                        break;
                    case commentString:
                        nextStep = getIdFromNextSteps(routingConfig, currentStepId[0], commentString);
                        break;
                    case rejectString:
                        nextStep = breakString;
                        break;
                    default:
                        nextStep = handleDefaultCase(routingConfig, currentStepId[0], codeSelected[0], decision[0]);
                        break;
                }
            }

            if(nextStep.isEmpty()){
                if (!routingConfig.getSteps().get(currentStepId[0]).subBP.isEmpty()){
                    nextSubBP = routingConfig.getSteps().get(currentStepId[0]).subBP;
                }else {
                    nextStep = breakString;
                }
            }else {
                if(!nextStep.equals(breakString)){
                    if (routingConfig.getSteps().containsKey(nextStep)) {
                        nextRouter = routingConfig.getSteps().get(nextStep).getRouter();
                        nextPage = routingConfig.getSteps().get(nextStep).getPage();
                    }else{
                        throw new AppworkException(ResponseCode.MODULE_ROUTING_INPUTS_ERROR);
                    }

//                    String[] assignedCN = {""};
//                    ReflectionUtil.of(outputSchema).ifPresent("getAssignedCN", (s) -> {
//                        assignedCN[0] = (String) s;
//                    });
//                    if(assignedCN[0].isEmpty()){
//                        throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
//                    }
                }
            }

            if(receiverType[0].equals("multiple")) {
                nextSubBP = "ACA_SubBP_parallelTasks";
            }

            updateOutputSchemaExtraData(outputSchema, routingConfig, currentStepId[0]);
            updateOutputSchema(outputSchema, nextStep, nextPage, nextSubBP, nextRouter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("ModuleRouting: "+ e.getMessage());
            throw new AppworkException(e.getMessage(),ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    private String getDecisionString(String decision){
        if(decision.contains(requestModificationString)){
            return requestModificationString;
        }else if(decision.contains(approveString)){
            return approveString;
        }else if(decision.contains(commentString)){
            return commentString;
        }else if(decision.contains(rejectString)){
            return rejectString;
        }else {
            return decision;
        }
    }

    private String handleApprovalCase(RoutingConfig routingConfig, String currentStepId, String codeSelected){
        String step = getIdFromNextSteps(routingConfig, currentStepId, codeSelected);
        if (step.isEmpty()) {
            return getIdFromNextSteps(routingConfig, currentStepId, approveString);
        }
        return step;
    }

    private String handleRedirectCase(RoutingConfig routingConfig, String currentStepId, String codeSelected){
        String step = getIdFromNextSteps(routingConfig, currentStepId, codeSelected);
        if (step.isEmpty()) {
            return getIdFromNextSteps(routingConfig, currentStepId, redirectString);
        }
        return step;
    }

    private <T> String handleRequestModificationCase(T outputSchema, RoutingConfig routingConfig, String currentStepId, String parentHistoryId){
        String step = getIdFromNextSteps(routingConfig, currentStepId, requestModificationString);
        if(!step.isEmpty()){
            ((OutputSchema) outputSchema).setParentHistoryId(parentHistoryId);
            return step;
        }
        else {
            return calculateFromApprovalHistory(outputSchema, parentHistoryId);
        }
    }

    private String handleDefaultCase(RoutingConfig routingConfig, String currentStepId, String codeSelected, String decision){
        String step = getIdFromNextSteps(routingConfig, currentStepId, codeSelected);
        if(step.isEmpty()){
            return getIdFromNextSteps(routingConfig, currentStepId, decision);
        }
        return step;
    }

    private String getIdFromNextSteps(RoutingConfig routingConfig, String currentStepId, String inputCase){
        if (routingConfig.getSteps().get(currentStepId).getNextStep().containsKey(inputCase) ) {
            return routingConfig.getSteps().get(currentStepId).getNextStep().get(inputCase).toString();
        }
        return "";
    }

    private <T> void updateOutputSchema(T outputSchema, String nextStep, String nextPage, String nextSubBP, Router nextRouter) {
        ((OutputSchema) outputSchema).setStepId(nextStep);
        ((OutputSchema) outputSchema).setPage(nextPage);
        ((OutputSchema) outputSchema).setSubBP(nextSubBP);
        ((OutputSchema) outputSchema).setRouter(nextRouter);
    }

    private <T> void updateOutputSchemaExtraData(T outputSchema, RoutingConfig routingConfig, String currentStep){
        HashMap<String,T> extraData = new HashMap<>();
        ReflectionUtil.of(outputSchema).ifPresent("getExtraData", (s) -> {
            extraData.putAll((HashMap<String,T>) s);
        });
        extraData.putAll(routingConfig.getSteps().get(currentStep).getExtraData());

        ((OutputSchema) outputSchema).setExtraData(extraData);

        ((OutputSchema) outputSchema).setAddApproval(routingConfig.getSteps().get(currentStep).getAddApproval());
    }

    private <T> String calculateFromApprovalHistory(T outputSchema, String parentHistoryId) {
        Optional<ApprovalHistory> approvalHistory = this.approvalHistoryRepository.findById(Long.parseLong(parentHistoryId));

        if (approvalHistory.isPresent()) {
            ((OutputSchema) outputSchema).setParentHistoryId(approvalHistory.get().getParent());
            ((OutputSchema) outputSchema).setAssignedCN(approvalHistory.get().getUserCN());
            return approvalHistory.get().getStepId();
        }

        return "";
    }
}


//           // Note: If there isn't assigned CN
//            if (assignedCN[0].isEmpty() && subBP.isEmpty()) {
//                try {
//                    Group parent = calculateParent();
//                    ((OutputSchema) outputSchema).setAssignedCN(parent.getCn());
//
//                    // Note: if Parent GroupCode is in next steps
//                    if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(parent.getGroupCode())) {
//                        nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(parent.getGroupCode());
//                    }
//                    // Note: if parent UnitTypeCode is in next steps
//                    else if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(parent.getUnit().getUnitTypeCode())) {
//                        nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(parent.getUnit().getUnitTypeCode());
//                    }
//                    else nextStep = breakString;
//                }
//                catch (AppworkException e){
//                    nextStep = breakString;
//                }
//            }
//===============================================================
//            private Group calculateParent() throws AppworkException {
//                User user = orgChartService.getUserByUsername(account.getUsername());
//                Optional<Group> userGroup = user.getGroup().stream().findFirst();
//                if (userGroup.isPresent()) {
//                    return orgChartService.getGroupParent(userGroup.get().getGroupCode());
//                }else {
//                    throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
//                }
//            }