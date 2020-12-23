package com.asset.appwork.controller;

import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import lombok.Data;
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

    // TODO: READ ON STATIC INNER CLASSES
    @Data
    private static class Request{
        RequestEntity requestEntity;
        OutputSchema processModel;
    }

    @PostMapping("/initiate")
    public ResponseEntity<AppResponse<String>> initiate(@RequestHeader("X-Auth-Token") String token, @RequestBody Request requestJson) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            // Entity Creation
            String entityName = "ACA_Entity_request";
            Entity entity = new Entity(account,
                    SystemUtil.generateRestAPIBaseUrl(environment,"AssetGeneralACA")
                    ,entityName);
            String entityCreateResponse = entity.create(requestJson.requestEntity);
            String entityId = SystemUtil.getJsonByPtrExpr(entityCreateResponse, "/Identity/Id");

            //Get Next Step
            requestJson.processModel.setProcess("process-1");
            requestJson.processModel.setStepId("init");
            requestJson.processModel.setEntityName(entityName);
            requestJson.processModel.setEntityId(entityId);

            String config = SystemUtil.readFile(environment.getProperty("process.config") + "\\" + requestJson.processModel.getProcess() + ".json");
            String cordysUrl = cordysService.getCordysUrl();

            ModuleRouting moduleRouting = new ModuleRouting( account, cordysUrl, config);
            moduleRouting.goToNext(requestJson.processModel);
            respBuilder.data("success");
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (Exception e){
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("/complete")
    public ResponseEntity<AppResponse<String>> complete(@RequestHeader("X-Auth-Token") String token, @RequestBody OutputSchema outputSchema) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            String config = SystemUtil.readFile(environment.getProperty("process.config") + "\\" + outputSchema.getProcess() + ".json");
            String cordysUrl = cordysService.getCordysUrl();

            ModuleRouting moduleRouting = new ModuleRouting( account, cordysUrl, config);
            moduleRouting.goToNext(outputSchema);
            respBuilder.data("success");
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
