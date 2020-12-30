package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;

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
            String cordysUrl = cordysService.getCordysUrl();

            // Entity Creation
            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment,"AssetGeneralACA");
            Entity entity = new Entity(account,
                    restAPIBaseUrl
                    , requestJson.processModel.getEntityName());
            Long entityId = entity.create(requestJson.requestEntity);

            //Get Next Step
            requestJson.processModel.setEntityId(entityId.toString());

            String filePath = requestJson.processModel.getProcessFilePath(environment.getProperty("process.config"));
            String config = SystemUtil.readFile(filePath);

            ModuleRouting moduleRouting = new ModuleRouting( account, cordysUrl, config, approvalHistoryRepository);
            String response = moduleRouting.goToNext(requestJson.processModel);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("/complete")
    public ResponseEntity<AppResponse<String>> complete(@RequestHeader("X-Auth-Token") String token, @RequestBody OutputSchema outputSchema) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
             Account account = tokenService.get(token);
            String cordysUrl = cordysService.getCordysUrl();

            String filePath = outputSchema.getProcessFilePath(environment.getProperty("process.config"));
            String config = SystemUtil.readFile(filePath);

            ModuleRouting moduleRouting = new ModuleRouting(account, cordysUrl, config, approvalHistoryRepository);
            String response=  moduleRouting.goToNext(outputSchema);
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
