package com.asset.appwork.controller;


import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.CordysUtil;
import com.asset.appwork.util.SystemUtil;
import com.asset.appwork.webservice.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/entity")
@RestController
public class EntityController {

    @Autowired
    TokenService tokenService;
    @Autowired
    CordysUtil cordysUtil;

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
                String response = cordysUtil.sendRequest(account, entity.readEntity(account.getSAMLart(), projectName, entityName, entityId));
                response = SystemUtil.convertXMLtoJSON(response);
                respBuilder.data(SystemUtil.convertStringToJsonNode(response));
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

}
