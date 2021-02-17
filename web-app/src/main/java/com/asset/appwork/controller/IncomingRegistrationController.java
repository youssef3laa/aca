package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.IncomingCase;
import com.asset.appwork.model.IncomingRegistration;
import com.asset.appwork.model.User;
import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.service.IncomingRegistrationService;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.service.RequestService;
import com.asset.appwork.util.SystemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/incoming/registration")

@Slf4j
@RestController
public class IncomingRegistrationController {

    @Autowired
    Environment environment;
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    ModuleRouting moduleRouting;
    @Autowired
    RequestService requestService;
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    IncomingRegistrationService incomingRegistrationService;
    @Autowired
    OrgChartService orgChartService;

    @Data
    private static class Request {
        IncomingRegistration incomingRegistration;
        IncomingCase incomingCase;
        OutputSchema outputSchema;
    }

    @GetMapping("read/{entityId}")
    public ResponseEntity<AppResponse<IncomingRegistration>> getById(@RequestHeader("X-Auth-Token") String token, @PathVariable Long entityId){
        AppResponse.ResponseBuilder<IncomingRegistration> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) throw new AppworkException(ResponseCode.INVALID_TOKEN);

            IncomingRegistration incomingRegistration = incomingRegistrationService.findById(entityId);
            respBuilder.data(incomingRegistration);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PostMapping("/case/initiate")
    public ResponseEntity<AppResponse<String>> initiate(@RequestHeader("X-Auth-Token") String token, @RequestBody Request request) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);

            User user = orgChartService.getLoggedInUser(account);
            Optional<Group> group = user.getGroup().stream().findFirst();
            String userCN = user.getCN();
            if(group.isPresent()){
                userCN = group.get().getCn();
            }

            String cordysUrl = cordysService.getCordysUrl();

            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
            Entity entity = new Entity(account, restAPIBaseUrl, IncomingCase.table);
            Long caseId = entity.create(request.getIncomingCase());

            entity = new Entity(account, restAPIBaseUrl, IncomingRegistration.table);
            request.incomingRegistration.setJobEntityId(caseId.toString());
            Long incomingId = entity.create(request.incomingRegistration);

            requestService.updateRequest(request.outputSchema, userCN, incomingId.toString(), request.incomingRegistration.getSubject(), "initiated");

            String response = moduleRouting.goToNext(request.outputSchema, account, cordysUrl);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }
}
