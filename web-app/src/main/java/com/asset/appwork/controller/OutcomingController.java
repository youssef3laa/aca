package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Outcoming;
import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.repository.OutcomingRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.service.IncomingRegistrationService;
import com.asset.appwork.service.OutcomingService;
import com.asset.appwork.util.SystemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/outcoming")

@Slf4j
@RestController
public class OutcomingController {

    @Autowired
    TokenService tokenService;
    @Autowired
    Environment environment;
    @Autowired
    CordysService cordysService;
    @Autowired
    ModuleRouting moduleRouting;
    @Autowired
    OutcomingService outcomingService;
    @Autowired
    OutcomingRepository outcomingRepository;
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    IncomingRegistrationService incomingRegistrationService;

    @Data
    private static class Request {
        Outcoming outcoming;
        OutputSchema outputSchema;
        Long registrationId;
    }

    @GetMapping("/read/{outcomingId}")
    public ResponseEntity<AppResponse<Outcoming>> getRequestById(@RequestHeader("X-Auth-Token") String token,
                                                                 @PathVariable Long outcomingId) {
        AppResponse.ResponseBuilder<Outcoming> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            Outcoming outcoming = outcomingService.findById(outcomingId);
            respBuilder.data(outcoming);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/create/temp")
    public ResponseEntity<AppResponse<Outcoming>> createTempRequest(@RequestHeader("X-Auth-Token") String token) {
        AppResponse.ResponseBuilder<Outcoming> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, environment.getProperty("aca.general.solution"));
            Entity entity = new Entity(account, restAPIBaseUrl, Outcoming.TABLE);
            Outcoming outcoming = new Outcoming();
            Long outcomingId = entity.create(outcoming);
            outcoming.setId(outcomingId);
//            Outcoming requestOptional = outcomingService.findById(outcomingId);
            respBuilder.data(outcoming);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PostMapping("/complete")
    public ResponseEntity<AppResponse<String>> complete(@RequestHeader("X-Auth-Token") String token, @RequestBody Request request) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);

            request.outcoming.setOutcomingNumber(outcomingService.generateRequestNumber());
            outcomingRepository.save(request.outcoming);
            incomingRegistrationService.updateWithOutcomingId(request.registrationId, request.outcoming.getId());
            String cordysUrl = cordysService.getCordysUrl();
            String response = moduleRouting.goToNext(request.outputSchema, account, cordysUrl);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

}
