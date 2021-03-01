package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Unit;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.ApprovalHistoryService;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.service.UserService;
import com.asset.appwork.util.SystemUtil;
import com.asset.appwork.platform.soap.Workflow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Created by karim on 11/4/20.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/workflow")
@RestController
public class WorkflowController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    ApprovalHistoryService approvalHistoryService;
    @Autowired
    UserService userService;

    @Transactional
    @GetMapping("/human/tasks")
    public ResponseEntity<AppResponse<JsonNode>> getHumanTask(@RequestHeader("X-Auth-Token") String token) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Workflow workflow = new Workflow();
            Account account = tokenService.get(token);
            ObjectMapper objectMapper = new ObjectMapper();
            if (account != null) {
                String response = cordysService.sendRequest(account, workflow.getHumanTasks());
                Document document = SystemUtil.convertStringToXMLDocument(response);
                NodeList tasks = document.getElementsByTagName("NOTF_TASK_INSTANCE");
                ArrayNode arrayNode = objectMapper.createArrayNode();
                if (tasks.getLength() > 0) {
                    for (int i = 0; i < tasks.getLength(); i++) {
                        JsonNode jsonNode = SystemUtil.convertDocumentNodeToJsonNode(tasks.item(i));

                        Optional<Unit> senderUnit = userService.getUnitByCN(jsonNode.get("Sender").get("").textValue());
                        if(senderUnit.isPresent()){
                            ((ObjectNode) jsonNode.get("Sender")).put("unit", senderUnit.get().getNameAr());
                        }else{
                            ((ObjectNode) jsonNode.get("Sender")).put("unit", "");
                        }

                        Optional<Unit> assigneeUnit = userService.getUnitByCN(jsonNode.get("Target").get("").textValue());
                        if(assigneeUnit.isPresent()){
                            ((ObjectNode) jsonNode.get("Target")).put("unit", assigneeUnit.get().getNameAr());
                        }else{
                            ((ObjectNode) jsonNode.get("Target")).put("unit", "");

                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date deliveryDate = simpleDateFormat.parse(jsonNode.get("DeliveryDate").textValue());
                        simpleDateFormat.applyPattern("yyyy-MM-dd , hh:mm:ss a");
                        ((ObjectNode) jsonNode).put("DeliveryDate", simpleDateFormat.format(deliveryDate));

                        arrayNode.add(jsonNode);
                    }
                }

                respBuilder.data(arrayNode);
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("/complete")
    public ResponseEntity<AppResponse<String>> completeWorkflow(@RequestHeader("X-Auth-Token") String token, @RequestBody() String taskJson) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Workflow workflow = new Workflow();
            Account account = tokenService.get(token);
            String taskId = SystemUtil.readJSONField(taskJson, "TaskId");
            String nameSpace = SystemUtil.readJSONField(taskJson, "NameSpace");
            String taskData = SystemUtil.readJSONObject(taskJson, "TaskData");
            taskData = SystemUtil.convertJSONtoXML(taskData);
            taskData = SystemUtil.addNameSpaceToXML(taskData, nameSpace);
            if (account != null) {
                String response = cordysService.sendRequest(account, workflow.performTaskAction(taskId, "COMPLETE", "", taskData));
                respBuilder.data(response);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("/task/claim")
    public ResponseEntity<AppResponse<String>> claimTask(@RequestHeader("X-Auth-Token") String token, @RequestBody() Request request) {
        AppResponse.ResponseBuilder<String> responseBuilder = AppResponse.builder();
        try {
            Workflow workflow = new Workflow();
            Account account = tokenService.get(token);
            if (account != null) {
                String response = cordysService.sendRequest(account, workflow.getTask(account.getSAMLart(), request.taskId));
                System.out.println(response);

                Document document = SystemUtil.convertStringToXMLDocument(response);
                Node task = null;
                if (document != null) {
                    task = document.getElementsByTagName("Task").item(0);
                    response = SystemUtil.convertDocumentNodetoJSON(task);
                    String taskState = SystemUtil.readJSONField(response, "State");
                    if (taskState != null) {
                        if (!taskState.equals("ASSIGNED")) {
                            approvalHistoryService.updateReceiveDate(Long.parseLong(request.approvalId));
                            response = cordysService.sendRequest(account, workflow.claimTask(account.getSAMLart(), request.taskId));
                        } else {
                            response = "Task is already claimed.";
                        }
                    } else {
                        throw new AppworkException("Invalid Task State Response", ResponseCode.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    throw new AppworkException("Invalid Task State Response", ResponseCode.INTERNAL_SERVER_ERROR);
                }
                responseBuilder.data(response);
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            responseBuilder.status(e.getCode());
            responseBuilder.data(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }

    @GetMapping("/task/data")
    public ResponseEntity<AppResponse<JsonNode>> getTaskData(@RequestHeader("X-Auth-Token") String token, @RequestParam() String taskId) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            Workflow workflow = new Workflow();
            Account account = tokenService.get(token);
            if (account != null) {
                String response = cordysService.sendRequest(account, workflow.getTask(account.getSAMLart(), taskId));
                System.out.println(response);
                response = SystemUtil.convertXMLtoJSON(response);
                responseBuilder.data(SystemUtil.convertStringToJsonNode(response));
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            responseBuilder.status(e.getCode());
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBuilder.build().getResponseEntity();

    }

    @Data
    private static class Request {
        String taskId;
        String approvalId;
    }
}
