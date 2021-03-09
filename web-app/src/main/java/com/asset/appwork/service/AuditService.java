package com.asset.appwork.service;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Audit;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
@Slf4j
@Service
public class AuditService {

    @Autowired
    TokenService tokenService;
    @Autowired
    Environment environment;

    public ResponseEntity<AppResponse<String>> createAudit( String token,  Audit audit){
        AppResponse.ResponseBuilder<String> respBuidler = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuidler.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
            Entity entity = new Entity(account, restAPIBaseUrl, "ACA_Entity_Audit");
            Long entityId = entity.create(audit);
            respBuidler.data(entityId.toString());
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuidler.status(e.getCode());
        }

        return respBuidler.build().getResponseEntity();
    }

}
