package com.asset.appwork.orgchart;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

public class ModuleRouting {
    private final String breakString = "end";
    private final String approveString = "approve";
    private final String commentString = "comment";
    private final String requestModificationString = "requestModification";
    private final String rejectString = "reject";

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
        String processName;
    }

    @Data
    static class StepConfig {
        boolean hasAutocomplete;
        String roleFilter, unitFilter, userFilter;
        String page;
        String condition;
        HashMap<String, String> nextStep;
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
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
        return response;
    }

    private <T> void calculateNextStep(T outputSchema) throws AppworkException {
        //TODO: Create Setter function in reflection class
        try {
            String nextStep = "";

            RoutingConfig routingConfig = generateRoutingConfig();

            String[] currentStepId = {""};
            String[] codeSelected = {""};
            String[] decision = {""};
            String[] parentHistoryId = {""};
            String[] assignedCN = {""};

            ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s) -> {
                currentStepId[0] = (String) s;
            }).ifPresent("getCode", (s) -> {
                codeSelected[0] = (String) s;
            }).ifPresent("getDecision", (s) -> {
                decision[0] = (String) s;
            }).ifPresent("getParentHistoryId", (s) -> {
                parentHistoryId[0] = (String) s;
            }).ifPresent("getAssignedCN", (s) -> {
                assignedCN[0] = (String) s;
            });

            ((OutputSchema) outputSchema).setParentHistoryId(null);

            // Note: Else case Code is in Next Steps
            if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(codeSelected[0])) {
                nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(codeSelected[0]);
            }

            // Note: Case Request Modification
            else if (decision[0].contains(requestModificationString)) {
                // NOTE:  If code selected and is in nextSteps
                if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(requestModificationString)) {
                    ((OutputSchema) outputSchema).setParentHistoryId(parentHistoryId[0]);
                    nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(requestModificationString);
                }

                // Note: get previous step from approval history
                else {
                    nextStep = calculateFromApprovalHistory(outputSchema, parentHistoryId[0]);
                }
            }

            // Note: Else case Approve
            else if (decision[0].contains(approveString)) {
                if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(approveString)) {
                    nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(approveString);
                }
            }

            // Note: Else case Comment
            else if (decision[0].contains(commentString)) {
                if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(commentString)) {
                    nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(commentString);
                }
            }

            // Note: Else case decision is in Next Steps
            else if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(decision[0])) {
                nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(decision[0]);
            }

            else nextStep = breakString;


            // Note: If there isn't assigned CN
            if (assignedCN[0].isEmpty()) {
                try {
                    Group parent = calculateParent();
                    ((OutputSchema) outputSchema).setAssignedCN(parent.getCn());

                    // Note: if Parent GroupCode is in next steps
                    if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(parent.getGroupCode())) {
                        nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(parent.getGroupCode());
                    }
                    // Note: if parent UnitTypeCode is in next steps
                    else if (routingConfig.getSteps().get(currentStepId[0]).getNextStep().containsKey(parent.getUnit().getUnitTypeCode())) {
                        nextStep = routingConfig.getSteps().get(currentStepId[0]).getNextStep().get(parent.getUnit().getUnitTypeCode());
                    }
                    else nextStep = breakString;
                }
                catch (AppworkException e){
                    nextStep = breakString;
                }
            }

            // Note: If next step calculated is in the JSON
            if (routingConfig.getSteps().containsKey(nextStep)) {
                // Note: If next step contains roleFilter
                if (!routingConfig.getSteps().get(nextStep).getRoleFilter().isEmpty())
                    ((OutputSchema) outputSchema).setRoleFilter(routingConfig.getSteps().get(nextStep).getRoleFilter());
            }
            else nextStep = breakString;

            if (nextStep.isEmpty()) {
                nextStep = breakString;
            }

            if (!nextStep.equals(breakString)) {
                String nextPage = routingConfig.getSteps().get(nextStep).getPage();
                ((OutputSchema) outputSchema).setPage(nextPage);
            }

            ((OutputSchema) outputSchema).setStepId(nextStep);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new AppworkException(e.getMessage(),ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    private Group calculateParent() throws AppworkException {
            User user = orgChartService.getUserDetails(account.getUsername() + "@aw.aca");
            Optional<Group> userGroup = user.getGroup().stream().findFirst();
            if (userGroup.isPresent()) {
                return orgChartService.getGroupParent(userGroup.get().getGroupCode());
            }else {
                throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
            }
    }

    private <T> String calculateFromApprovalHistory(T outputSchema, String parentHistoryId) {
        Optional<ApprovalHistory> approvalHistory = this.approvalHistoryRepository.findById(Long.parseLong(parentHistoryId));

        if (approvalHistory.isPresent()) {
            ((OutputSchema) outputSchema).setParentHistoryId(approvalHistory.get().getParent());
            ((OutputSchema) outputSchema).setAssignedCN(approvalHistory.get().getUserCN());
            return approvalHistory.get().getStepId();
        }

        return breakString;
    }
}
