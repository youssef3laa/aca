package com.asset.appwork.controller;

import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.CordysUtil;
import com.asset.appwork.webservice.Workflow;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by karim on 11/4/20.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/workflow")
@RestController
public class WorkflowController {
    @Autowired
    CordysManagement cordysManagement;
    @Autowired
    CordysUtil cordysUtil;
    @GetMapping("/human/tasks")
    public void getHumanTask(@RequestHeader("account") Account account){
        Workflow workflow = new Workflow();
        String response = null;
        try {
             response = cordysUtil.sendRequest(account, workflow.getHumanTasks(account.getSAMLart()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (AppworkException e) {
            e.printStackTrace();
        }
        System.out.println(account.getSAMLart());
    }
}
