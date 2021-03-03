package com.asset.appwork.orgchart;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.Assignees;
import com.asset.appwork.dto.Router;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.model.User;
import com.asset.appwork.platform.soap.Process;
import com.asset.appwork.platform.soap.Workflow;
import com.asset.appwork.platform.util.CordysUtil;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.repository.RequestRepository;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.util.ReflectionUtil;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ModuleRouting {

    @Autowired
    OrgChartService orgChartService;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    Environment environment;

    private final String breakString = "end";
    private final String rejectString = "reject";
    private final String approveString = "approve";
    private final String redirectString = "redirect";
    private final String commentString = "comment";
    private final String multipleString = "multiple";
    private final String parallelString = "parallel";
    private final String requestModificationString = "requestModification";
    private final String multipleResponsibleString = "responsible";

    @Data
    static class RoutingConfig {
        Map<String, StepConfig> steps;
        String processName = "";
    }

    @Data
    static class StepConfig<T> {
        Boolean addApproval = true;
        Router router = new Router();
        String type = "";
        String component = "", config = "", readonlyComponent = "";
        String subBP = "";
        HashMap<String, String> nextStep = new HashMap<>();
        HashMap<String, List<String>> nextPhase = new HashMap<>();
        HashMap<String, T> extraData = new HashMap<>();
    }

    private <T> RoutingConfig generateRoutingConfig(T outputSchema) throws IOException {
        String filePath = ((OutputSchema) outputSchema).getProcessFilePath(environment.getProperty("process.config"));
        String config = SystemUtil.readFile(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(config);
        return objectMapper.convertValue(jsonNode, RoutingConfig.class);
    }

    @Transactional
    public <T> String goToNext(T outputSchema, Account account, String cordysUrl) throws AppworkException {
        String[] currentStepId = {""};
        ReflectionUtil.of(outputSchema).ifPresent("getStepId", (s) -> {
            currentStepId[0] = (String) s;
        });
        calculateNextStep(outputSchema, account);
        if (currentStepId[0].equals("init"))
            return initiateProcess(outputSchema, account, cordysUrl);
        else
            return completeWorkflow(outputSchema, account, cordysUrl);
    }

    public <T> String calculateOutputSchema(T outputSchema, Account account) throws AppworkException {
        calculateNextStep(outputSchema, account);
        String[] xml = {""};
        ReflectionUtil.of(outputSchema).ifPresent("getXMLWithNameSpace", (s) -> {
            xml[0] = (String) s;
        });
        return xml[0];
    }

    private <T> String initiateProcess(T outputSchema, Account account, String cordysUrl) throws AppworkException {
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
            log.error("ModuleRouting: " + e.getMessage());
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
        return response;
    }

    private <T> String completeWorkflow(T outputSchema, Account account, String cordysUrl) throws AppworkException {
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
            log.error("ModuleRouting: " + e.getMessage());
            throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
        }
        return response;
    }

    private <T> void calculateNextStep(T outputSchema, Account account) throws AppworkException {
        //TODO: Create Setter function in reflection class
        try {
            String nextStep = "", nextComponent = "", nextConfig = "", nextReadonlyCompnent = "", nextSubBP = "";
            Router nextRouter = new Router();

            RoutingConfig routingConfig = generateRoutingConfig(outputSchema);

            String[] currentStepId = {""}, codeSelected = {""}, decision = {""}, parentHistoryId = {""}, receiverType = {""};

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
                if(!routingConfig.getSteps().get(currentStepId[0]).getNextPhase().isEmpty()){
                    nextStep = currentStepId[0];
                }else{
                    throw new AppworkException(ResponseCode.MODULE_ROUTING_INPUTS_ERROR);
                }
            }

            if(nextStep.isEmpty()){
                decision[0] = getDecisionString(decision[0]);

                if(receiverType[0].equals(multipleString)) {
                    nextSubBP = "ACA_SubBP_multipleUnits";
                    handleParallelTasks(outputSchema, routingConfig, currentStepId[0], multipleString);
                    decision[0] = multipleResponsibleString;
                }

                switch (decision[0]) {
                    case approveString:
                        nextStep = handleApprovalCase(routingConfig, currentStepId[0], codeSelected[0]);
                        break;
                    case redirectString:
                        nextStep = handleRedirectCase(routingConfig, currentStepId[0], codeSelected[0]);
                        break;
                    case requestModificationString:
    //                    nextType = "modification";
                        nextStep = handleRequestModificationCase(outputSchema, routingConfig, currentStepId[0], parentHistoryId[0]);
                        break;
                    case parallelString:
                        nextSubBP = "ACA_SubBP_parallelTasks";
                        nextStep = handleParallelTasks(outputSchema, routingConfig, currentStepId[0], parallelString);
                        break;
                    case commentString:
                        nextStep = handleCommentCase(routingConfig, currentStepId[0], codeSelected[0]);
                        break;
                    case rejectString:
                        nextStep = breakString;
                        break;
                    default:
                        nextStep = handleDefaultCase(routingConfig, currentStepId[0], codeSelected[0], decision[0]);
                        break;
                }
            }

            if (nextStep.isEmpty()) {
                if (!routingConfig.getSteps().get(currentStepId[0]).subBP.isEmpty()) {
                    nextSubBP = routingConfig.getSteps().get(currentStepId[0]).subBP;
                }
//                else {
//                    nextStep = breakString;
//                }
            } else if (!nextStep.equals(breakString)) {
                if (routingConfig.getSteps().containsKey(nextStep)) {
                    nextRouter = routingConfig.getSteps().get(nextStep).getRouter();
                    nextComponent = routingConfig.getSteps().get(nextStep).getComponent();
                    nextConfig = routingConfig.getSteps().get(nextStep).getConfig();
                    nextReadonlyCompnent = routingConfig.getSteps().get(nextStep).getReadonlyComponent();
//                    if(nextType.isEmpty()) nextType = routingConfig.getSteps().get(nextStep).getType();
                } else {
                    throw new AppworkException(ResponseCode.MODULE_ROUTING_INPUTS_ERROR);
                }
//                String[] assignedCN = {""};
//                ReflectionUtil.of(outputSchema).ifPresent("getAssignedCN", (s) -> {
//                    assignedCN[0] = (String) s;
//                });
//                if(assignedCN[0].isEmpty()){
//                    throw new AppworkException(ResponseCode.MODULE_ROUTING_FAILURE);
//                }
            }

            updateOutputSchemaExtraData(outputSchema, routingConfig, currentStepId[0]);
            updateOutputSchema(outputSchema, nextStep,
                    nextComponent, nextConfig, nextReadonlyCompnent, nextSubBP, nextRouter);

            addUserToRequest(outputSchema, account);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ModuleRouting: " + e.getMessage());
            throw new AppworkException(e.getMessage(), ResponseCode.MODULE_ROUTING_FAILURE);
        }
    }

    private String getDecisionString(String decision) {
        if (decision.contains(requestModificationString)) {
            return requestModificationString;
        } else if (decision.contains(approveString)) {
            return approveString;
        } else if (decision.contains(commentString)) {
            return commentString;
        } else if (decision.contains(rejectString)) {
            return rejectString;
        } else {
            return decision;
        }
    }

    private String handleApprovalCase(RoutingConfig routingConfig, String currentStepId, String codeSelected) {
        String step = getIdFromNextSteps(routingConfig, currentStepId, codeSelected);
        if (step.isEmpty()) {
            return getIdFromNextSteps(routingConfig, currentStepId, approveString);
        }
        return step;
    }

    private String handleCommentCase(RoutingConfig routingConfig, String currentStepId, String codeSelected) {
        String step = getIdFromNextSteps(routingConfig, currentStepId, codeSelected);
        if (step.isEmpty()) {
            return getIdFromNextSteps(routingConfig, currentStepId, commentString);
        }
        return step;
    }

    private String handleRedirectCase(RoutingConfig routingConfig, String currentStepId, String codeSelected) {
        String step = getIdFromNextSteps(routingConfig, currentStepId, codeSelected);
        if (step.isEmpty()) {
            return getIdFromNextSteps(routingConfig, currentStepId, redirectString);
        }
        return step;
    }

    private <T> String handleRequestModificationCase(T outputSchema, RoutingConfig routingConfig, String currentStepId, String parentHistoryId) {
        String step = getIdFromNextSteps(routingConfig, currentStepId, requestModificationString);
        if(!step.isEmpty()){
            if(step.equals(currentStepId)){
                return calculateFromApprovalHistory(outputSchema, parentHistoryId);
            }
//            ((OutputSchema) outputSchema).setParentHistoryId(parentHistoryId);
            return step;
        }
        return calculateFromApprovalHistory(outputSchema, parentHistoryId);
    }

    private String handleDefaultCase(RoutingConfig routingConfig, String currentStepId, String codeSelected, String decision) {
        String step = getIdFromNextSteps(routingConfig, currentStepId, codeSelected);
        if (step.isEmpty()) {
            return getIdFromNextSteps(routingConfig, currentStepId, decision);
        }
        return step;
    }

    private <T> String handleParallelTasks(T outputSchema, RoutingConfig routingConfig, String currentStepId, String decision) {
        String multipleNextStep = getIdFromNextSteps(routingConfig, currentStepId, decision);
        if (!multipleNextStep.isEmpty() && !multipleNextStep.equals(breakString)) {
            if (routingConfig.getSteps().containsKey(multipleNextStep)) {
                Router multipleNextRouter = routingConfig.getSteps().get(multipleNextStep).getRouter();
                String multipleNextComponent = routingConfig.getSteps().get(multipleNextStep).getComponent();
                String multipleNextConfig = routingConfig.getSteps().get(multipleNextStep).getConfig();
                String multipleNextReadOnlyComponent = routingConfig.getSteps().get(multipleNextStep).getReadonlyComponent();
                updateOutputSchemaAssignees(outputSchema, multipleNextStep,
                        multipleNextComponent, multipleNextConfig, multipleNextReadOnlyComponent, multipleNextRouter);
            }
        }
        return multipleNextStep;
    }

    private String getIdFromNextSteps(RoutingConfig routingConfig, String currentStepId, String inputCase){
        if(!routingConfig.getSteps().get(currentStepId).getNextPhase().isEmpty()){
            for(Object key: routingConfig.getSteps().get(currentStepId).getNextPhase().keySet()) {
                String keyString = (String)key;
                if(((List<String>)routingConfig.getSteps().get(currentStepId).getNextPhase().get(keyString)).contains(inputCase)){
                    return keyString;
                }
            }
            return currentStepId;
        }else if (routingConfig.getSteps().get(currentStepId).getNextStep().containsKey(inputCase) ) {
            return routingConfig.getSteps().get(currentStepId).getNextStep().get(inputCase).toString();
        }
        return "";
    }

    private <T> void updateOutputSchema(T outputSchema, String nextStep,
                                        String nextComponent, String nextConfig, String nextReadonlyComponent,
                                        String nextSubBP, Router nextRouter) {
        ((OutputSchema) outputSchema).setStepId(nextStep);
        ((OutputSchema) outputSchema).setComponent(nextComponent);
        ((OutputSchema) outputSchema).setConfig(nextConfig);
        ((OutputSchema) outputSchema).setReadonlyComponent(nextReadonlyComponent);
        ((OutputSchema) outputSchema).setSubBP(nextSubBP);
        ((OutputSchema) outputSchema).setRouter(nextRouter);
//        ((OutputSchema) outputSchema).setCaseType(nextType);
    }

    private <T> void updateOutputSchemaAssignees(T outputSchema, String stepId,
                                                 String component, String config, String readonlyComponent,
                                                 Router router) {
        Assignees[] assignees = {new Assignees()};
        String[] assignedCN = {""};
        ReflectionUtil.of(outputSchema).ifPresent("getAssignees", (s) -> {
            assignees[0] = (Assignees) s;
        }).ifPresent("getAssignedCN", (s) -> {
            assignedCN[0] = (String) s;
        });
        assignees[0].setOwner(assignedCN[0]);
        assignees[0].setStepId(stepId);
        assignees[0].setComponent(component);
        assignees[0].setConfig(config);
        assignees[0].setReadonlyComponent(readonlyComponent);
        assignees[0].setRouter(router);

        ((OutputSchema) outputSchema).setAssignees(assignees[0]);
    }

    private <T> void updateOutputSchemaExtraData(T outputSchema, RoutingConfig routingConfig, String currentStep) {
        HashMap<String, T> extraData = new HashMap<>();
        ReflectionUtil.of(outputSchema).ifPresent("getExtraData", (s) -> {
            extraData.putAll((HashMap<String, T>) s);
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

    @Transactional
    <T> void addUserToRequest(T outputSchema, Account account) throws AppworkException {
        String[] requestId = {""};
        ReflectionUtil.of(outputSchema).ifPresent("getRequestId", (s) -> {
            requestId[0] = (String) s;
        });

        if (!requestId[0].isEmpty()) {
            Optional<RequestEntity> request = requestRepository.findById(Long.parseLong(requestId[0]));
            if (request.isPresent()) {
                String users = "";
                String roles = "";
                if (request.get().getWorkingUsers() == null) {
                    users += ";";
                    roles += ";";
                } else {
                    users = request.get().getWorkingUsers();
                    roles = request.get().getWorkingRoles();
                }
                User userData = orgChartService.getLoggedInUser(account);
                Optional<Group> groupData = userData.getGroup().stream().findFirst();

                users += userData.getId() + ";";
                if (groupData.isPresent()) {
                    roles += groupData.get().getGroupCode() + ";";
                }

                request.get().setWorkingUsers(users);
                request.get().setWorkingRoles(roles);

                requestRepository.save(request.get());
            }
        }
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