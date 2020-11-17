package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.CordysUtil;
import com.asset.appwork.util.SystemUtil;
import com.asset.appwork.webservice.Task;
import com.asset.appwork.webservice.Workflow;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
    CordysUtil cordysUtil;
    @GetMapping("/human/tasks")
    public ResponseEntity<AppResponse<String>> getHumanTask(@RequestHeader("X-Auth-Token") String token){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Workflow workflow = new Workflow();
            Account account = tokenService.get(token);
            if(account != null){
                String response = cordysUtil.sendRequest(account, workflow.getHumanTasks(account.getSAMLart()));
                Document document = SystemUtil.convertStringToXMLDocument(response);
                NodeList tasks = document.getElementsByTagName("NOTF_TASK_INSTANCE");
                String data = "{\n" +
                        "\"data\": [\n";
//                String data = "[\n";
                if(tasks.getLength() > 0){
                    for(int i = 0 ; i < tasks.getLength() ; i++){
                        data += SystemUtil.converDocumentNodetoJSON(tasks.item(i))+",\n";
                    }
                    data = data.substring(0,data.length()-2);
                }
//                data += "]";
                data += "]}";
                respBuilder.data(data);
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

    @PostMapping("/complete")
    public ResponseEntity<AppResponse<String>> completeWorkflow(@RequestHeader("X-Auth-Token") String token,@RequestBody() String taskJson) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Task task= new Task();
            Account account = tokenService.get(token);
            String taskId = SystemUtil.readJSONField(taskJson, "TaskId");
            String nameSpace = SystemUtil.readJSONField(taskJson, "NameSpace");
            String taskData = SystemUtil.readJSONObject(taskJson, "TaskData");
            taskData = SystemUtil.convertJSONtoXML(taskData);
            taskData = SystemUtil.addNameSpaceToXML(taskData,nameSpace);
            if(account != null){
                String response = cordysUtil.sendRequest(account, task.performTaskAction(account.getSAMLart(), taskId, "COMPLETE", "", taskData));
                respBuilder.data(response);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }
}
