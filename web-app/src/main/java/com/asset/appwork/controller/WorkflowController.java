package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.CordysUtil;
import com.asset.appwork.webservice.Workflow;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Workflow workflow = new Workflow();
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        String response = null;
        try {
            Account account = tokenService.readTokenData(token);
            if(account != null){
                response = cordysUtil.sendRequest(account, workflow.getHumanTasks(account.getSAMLart()));
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
//        System.out.println(account.getSAMLart());
    }
}
