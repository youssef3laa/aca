package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.EscalationService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/escalation")
@RestController
@Slf4j
public class EscalationController {

    @Autowired
    TokenService tokenService;

    @Autowired
    EscalationService escalationService;

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<AppResponse<JsonNode>> createEscalation(@RequestHeader("X-Auth-Token") String token,
                                                                  @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(escalationService.createEscalation(account, props).toString()));
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readEscalation(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(escalationService.getEscalation(id).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<AppResponse<JsonNode>> updateEscalation(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable Long id,
                                                                  @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(escalationService.updateEscalation(id, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteEscalation(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            escalationService.deleteEscalation(id);
            respBuilder.info("infoMessage", "Deleted Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }
}
