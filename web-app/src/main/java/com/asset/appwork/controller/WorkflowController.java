package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import com.asset.appwork.platform.soap.Workflow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;

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

    @GetMapping("/human/tasks")
    public ResponseEntity<AppResponse<String>> getHumanTask(@RequestHeader("X-Auth-Token") String token) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Workflow workflow = new Workflow();
            Account account = tokenService.get(token);
            if (account != null) {
                String response = cordysService.sendRequest(account, workflow.getHumanTasks());
                Document document = SystemUtil.convertStringToXMLDocument(response);
                NodeList tasks = document.getElementsByTagName("NOTF_TASK_INSTANCE");
                String data = "{\n" +
                        "\"data\": [\n";
//                String data = "[\n";
                if (tasks.getLength() > 0) {
                    for (int i = 0; i < tasks.getLength(); i++) {
                        data += SystemUtil.convertDocumentNodetoJSON(tasks.item(i)) + ",\n";
                    }
                    data = data.substring(0, data.length() - 2);
                }
//                data += "]";
                data += "]\n}";
                respBuilder.data(data);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
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
                String response = cordysService.sendRequest(account, workflow.performTaskAction(account.getSAMLart(), taskId, "COMPLETE", "", taskData));
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
    public ResponseEntity<AppResponse<String>> claimTask(@RequestHeader("X-Auth-Token") String token, @RequestBody() String taskId) {
        AppResponse.ResponseBuilder<String> responseBuilder = AppResponse.builder();
        try {
            Workflow workflow = new Workflow();
            Account account = tokenService.get(token);
            if (account != null) {
                String response = cordysService.sendRequest(account, workflow.getTask(account.getSAMLart(), taskId));
                System.out.println(response);

                Document document = SystemUtil.convertStringToXMLDocument(response);
                Node task = null;
                if (document != null) {
                    task = document.getElementsByTagName("Task").item(0);
                    response = SystemUtil.convertDocumentNodetoJSON(task);
                    String taskState = SystemUtil.readJSONField(response, "State");
                    if (taskState != null) {
                        if (!taskState.equals("ASSIGNED")) {
                            response = cordysService.sendRequest(account, workflow.claimTask(account.getSAMLart(), taskId));
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

}
