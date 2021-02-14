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
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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
                                                                     @PathVariable Long outcomingId){
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
            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
            Entity entity = new Entity(account, restAPIBaseUrl, "ACA_Entity_Outcoming_Data");

            Outcoming outcoming = new Outcoming();
            outcoming.setOutcomingNumber(outcomingService.generateRequestNumber(account));
            outcoming.setOutcomingDate(new Date());

            Long outcomingId = entity.create(outcoming);
            Outcoming requestOptional = outcomingService.findById(outcomingId);

            respBuilder.data(requestOptional);
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

            outcomingRepository.save(request.outcoming);
            incomingRegistrationService.updateWithOutcomingId(request.registrationId, request.outcoming.getId());

            String cordysUrl = cordysService.getCordysUrl();

            String filePath = request.outputSchema.getProcessFilePath(environment.getProperty("process.config"));
            String config = SystemUtil.readFile(filePath);

            ModuleRouting moduleRouting = new ModuleRouting(account, cordysUrl, config, approvalHistoryRepository);
            String response = moduleRouting.goToNext(request.outputSchema);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return respBuilder.build().getResponseEntity();
    }

}
