package com.asset.appwork.controller;

import com.asset.appwork.ModuleRouting;
import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.platform.soap.Process;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/process")

@RestController
public class ProcessController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    Environment environment;

    @PostMapping("/initiate")
    public ResponseEntity<AppResponse<String>> initiate(@RequestHeader("X-Auth-Token") String token, @RequestBody String requestJson) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            // Entity Creation
            String entityName = SystemUtil.readJSONField(requestJson, "entityName");
            String entityModel = SystemUtil.readJSONObject(requestJson, "entityModel");
            Entity entity = new Entity(account,
                    SystemUtil.generateRestAPIBaseUrl(environment,"AssetGeneralACA")
                    ,entityName);
            String entityCreateResponse = entity.create(entityModel);

            //Get Next Step
            ModuleRouting moduleRouting = new ModuleRouting(environment.getProperty("process.config"),"process-1");
            String nextStep = moduleRouting.calculateNextStep("init","Aly");
            respBuilder.data(nextStep);

            // Process Initiation
            String params = SystemUtil.readJSONObject(requestJson, "processModel");
            params = SystemUtil.convertJSONtoXML(params);
            String processInitiateMessage = new Process().initiate(account.getTicket(), "ACA_BP_processRouting", params);
            String response = cordysService.sendRequest(account,
                    processInitiateMessage);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (Exception e){
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return respBuilder.build().getResponseEntity();
    }
}
