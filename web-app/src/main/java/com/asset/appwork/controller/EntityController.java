package com.asset.appwork.controller;


import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/entity")
@RestController
public class EntityController {
    @Autowired
    Environment environment;
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;

    @SuppressWarnings("DuplicatedCode")
    @GetMapping("/read")
    public ResponseEntity<AppResponse<String>> readEntity(@RequestHeader("X-Auth-Token") String token
            , @RequestParam("entityName") String entityName
            , @RequestParam("entityId") Long entityId) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                String apiBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment,"AssetGeneralACA");
                Entity entity = new Entity(account, apiBaseUrl,entityName);
                String response = entity.readById(entityId);
                response = SystemUtil.readJSONObject(response, "Properties");
                respBuilder.data(response);
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }

}
