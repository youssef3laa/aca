package com.asset.appwork.controller;


import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import com.asset.appwork.platform.soap.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/entity")
@RestController
public class EntityController {

    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;

    @SuppressWarnings("DuplicatedCode")
    @GetMapping("/read")
    public ResponseEntity<AppResponse<JsonNode>> readEntity(@RequestHeader("X-Auth-Token") String token
            , @RequestParam("projectName") String projectName
            , @RequestParam("entityName") String entityName
            , @RequestParam("entityId") String entityId) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Entity entity = new Entity();
            Account account = tokenService.get(token);
            if (account != null) {
                String response = cordysService.sendRequest(account, entity.readEntity(account.getSAMLart(), projectName, entityName, entityId));
                response = SystemUtil.convertXMLtoJSON(response);
                respBuilder.data(SystemUtil.convertStringToJsonNode(response));
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

}
