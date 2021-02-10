package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.RequestEntity;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.service.RequestService;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/request")

@Slf4j
@RestController
public class RequestController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    Environment environment;
    @Autowired
    RequestService requestService;

    @GetMapping("/create/temp")
    public ResponseEntity<AppResponse<Long>> initiate(@RequestHeader("X-Auth-Token") String token) {
        AppResponse.ResponseBuilder<Long> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
            Entity entity = new Entity(account, restAPIBaseUrl, "ACA_Entity_request");

            RequestEntity request = new RequestEntity();
            request.setRequestNumber(requestService.generateRequestNumber(account));
            request.setStatus("created");

            Long requestId = entity.create(request);

            respBuilder.data(requestId);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/read/forLinkIncoming")
    public ResponseEntity<AppResponse<List<RequestEntity>>> getRequestEntitiesForLinkIncomingProcess(@RequestHeader("X-Auth-Token") String token,
                                                                                                     @RequestParam("process") String process,
                                                                                                     @RequestParam("requestDate") String requestDate,
                                                                                                     @RequestParam(value = "subject", required = false) String subject,
                                                                                                     @RequestParam(value = "requestNumber", required = false) String requestNumber) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        AppResponse.ResponseBuilder<List<RequestEntity>> responseBuilder = AppResponse.builder();

        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            responseBuilder.data(requestService.getRequestsByProcessAndDateAndSubjectAndRequestNumber(process, simpleDateFormat.parse(requestDate), subject, requestNumber));
        } catch (AppworkException e) {
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return responseBuilder.build().getResponseEntity();

    }
}