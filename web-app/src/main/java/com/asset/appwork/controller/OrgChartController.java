package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/entity/org")
@RestController
public class OrgChartController {

    @Autowired
    TokenService tokenService;
    @Autowired
    Environment env;

    @PostMapping("/units/readList/{listName}")
    public ResponseEntity<AppResponse<JsonNode>> readList(@RequestHeader("X-Auth-Token") String token,
                                                     @PathVariable("listName")String listName
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            System.out.println(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readList(listName)));
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
