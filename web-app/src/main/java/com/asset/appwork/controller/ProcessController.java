package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.GeneralProcessModel;
import com.asset.appwork.model.LinkIncoming;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.service.ProcessService;
import com.asset.appwork.service.RequestEntityService;
import com.asset.appwork.soup.ProcessSOAP;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/process")

@Slf4j
@RestController
public class ProcessController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    Environment environment;
    @Autowired
    ModuleRouting moduleRouting;
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    OrgChartService orgChartService;
    @Autowired
    RequestEntityService requestEntityService;
    @Autowired
    ProcessService processService;

//    @PostMapping("/initiate")
//    public ResponseEntity<AppResponse<String>> initiate(@RequestHeader("X-Auth-Token") String token, @RequestBody Request requestJson) {
//        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            String cordysUrl = cordysService.getCordysUrl();
//
//            //Note: Entity Creation
//            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
//            Entity entity = new Entity(account,
//                    restAPIBaseUrl
//                    , requestJson.processModel.getEntityName());
//            Long entityId = entity.create(requestJson.generalProcessEntity);
//
//            requestEntityService.updateRequest(requestJson.getProcessModel(), account.getUsername(), entityId.toString(),
//                    "new-incoming", "created",requestJson.ge);
//
//            String response = moduleRouting.goToNext(requestJson.processModel, account, cordysUrl);
//            respBuilder.data(response);
//        } catch (AppworkException e) {
//            e.printStackTrace();
//            respBuilder.status(e.getCode());
//        }
//
//        return respBuilder.build().getResponseEntity();
//    }


    @PostMapping("/initiateLinkedIncoming")
    public ResponseEntity<AppResponse<String>> initiateLinkedIncoming(@RequestHeader("X-Auth-Token") String token,
                                                                      @RequestBody Request requestJson) {
        // create linkedIncoming entity
        // set entityId -> create requestEntity ;
        String cordysUrl = cordysService.getCordysUrl();
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
            Entity entity = new Entity(account,
                    restAPIBaseUrl
                    , "ACA_Entity_linkIncoming");
            LinkIncoming linkIncoming = new LinkIncoming();

            linkIncoming.setSourceIncomingId(String.valueOf(requestJson.getProcessModel().getExtraData().get("sourceIncomingId")));
            linkIncoming.setTargetIncomingId(String.valueOf(requestJson.getProcessModel().getExtraData().get("targetIncomingId")));
            Long linkIncomingEntityId = entity.create(linkIncoming);


            String generatedRequestNumber = requestEntityService.generateRequestNumber();
            RequestEntity requestEntity = new RequestEntity();
            requestEntity.setEntityId(String.valueOf(linkIncomingEntityId));
            requestEntity.setEntityName("ACA_Entity_linkIncoming");
            requestEntity.setRequestNumber(generatedRequestNumber);
            requestEntity.setSubject(String.valueOf(requestJson.getProcessModel().getExtraData().get("subject")));
            requestEntity.setProcess("linkIncoming");
            requestEntity.setRequestDate(new Date());
            requestEntity.setInitiator(String.valueOf(requestJson.getProcessModel().getExtraData().get("initiatorId")));
            requestEntity.setStatus("created");
            entity = new Entity(account,
                    restAPIBaseUrl
                    , "ACA_Entity_request");
            Long requestEntityId = entity.create(requestEntity);
            entity = new Entity(account,
                    restAPIBaseUrl
                    , "ACA_Entity_linkIncoming");

            linkIncoming = new LinkIncoming();
            linkIncoming.setRequestEntityId(String.valueOf(requestEntityId));
            linkIncoming.setSourceIncomingId(String.valueOf(requestJson.getProcessModel().getExtraData().get("sourceIncomingId")));
            linkIncoming.setTargetIncomingId(String.valueOf(requestJson.getProcessModel().getExtraData().get("targetIncomingId")));
            linkIncoming.setSourceRequestId(String.valueOf(requestJson.getProcessModel().getExtraData().get("sourceRequestId")));
            linkIncoming.setTargetRequestId(String.valueOf(requestJson.getProcessModel().getExtraData().get("targetRequestId")));

            entity.update(linkIncomingEntityId, linkIncoming);

            requestJson.processModel.setRequestId(String.valueOf(requestEntityId));
            String response = moduleRouting.goToNext(requestJson.processModel, account, cordysUrl);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }


    @Transactional
    @PostMapping("/complete")
    public ResponseEntity<AppResponse<String>> complete(@RequestHeader("X-Auth-Token") String token, @RequestBody OutputSchema outputSchema) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            String cordysUrl = cordysService.getCordysUrl();

            String response = moduleRouting.goToNext(outputSchema, account, cordysUrl);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("/pause")
    public ResponseEntity<AppResponse<String>> pauseProcess(@RequestHeader("X-Auth-Token") String token, @RequestBody OutputSchema outputSchema) {
        AppResponse.ResponseBuilder<String> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) {
                throw new AppworkException(ResponseCode.UNAUTHORIZED);
            }
            processService.pauseProcess(account, outputSchema);
            responseBuilder.status(ResponseCode.SUCCESS);

        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (ParseException e) {
            e.printStackTrace();
            responseBuilder.status(ResponseCode.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return responseBuilder.build().getResponseEntity();
    }

    @PostMapping("{processId}/resume")
    public ResponseEntity<AppResponse<String>> resumeProcess(@RequestHeader("X-Auth-Token") String token, @PathVariable("processId") String processInstanceId) {
        AppResponse.ResponseBuilder<String> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) {
                throw new AppworkException(ResponseCode.UNAUTHORIZED);
            }
            String pauseProcessResponse = cordysService.sendRequest(account, new ProcessSOAP().resumeProcessMsg(processInstanceId));
            log.info("sent resume process to process: " + processInstanceId + "\n and the response is :\n" + pauseProcessResponse);
//            responseBuilder.status(2)
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return responseBuilder.build().getResponseEntity();
    }

    // TODO: READ ON STATIC INNER CLASSES
    @Data
    private static class Request {
        GeneralProcessModel generalProcessEntity;
        OutputSchema processModel;
    }

}
