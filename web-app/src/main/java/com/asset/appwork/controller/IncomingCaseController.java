package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.IncomingCase;
import com.asset.appwork.repository.IncomingCaseRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.IncomingCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/incoming/case")

@Slf4j
@RestController
public class IncomingCaseController {

    @Autowired
    TokenService tokenService;
    @Autowired
    IncomingCaseService incomingCaseService;

    @GetMapping("read/{entityId}")
    public ResponseEntity<AppResponse<IncomingCase>> getById(@RequestHeader("X-Auth-Token") String token, @PathVariable Long entityId){
        AppResponse.ResponseBuilder<IncomingCase> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) throw new AppworkException(ResponseCode.INVALID_TOKEN);

            IncomingCase incomingCase = incomingCaseService.findById(entityId);
            respBuilder.data(incomingCase);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }
}
