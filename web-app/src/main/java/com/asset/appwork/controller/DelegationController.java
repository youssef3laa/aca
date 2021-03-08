package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Delegation;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.DelegationService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/delegation")
@RestController
@Slf4j
public class DelegationController {

    @Autowired
    TokenService tokenService;

    @Autowired
    DelegationService delegationService;

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<AppResponse<JsonNode>> createDelegation(@RequestHeader("X-Auth-Token") String token,
                                                                  @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(delegationService.createDelegation(account, props).toString()));
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
    public ResponseEntity<AppResponse<JsonNode>> readDelegation(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(delegationService.getDelegation(id).toString()));
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
    public ResponseEntity<AppResponse<JsonNode>> updateDelegation(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable Long id,
                                                                  @RequestBody Delegation delegation
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(delegationService.updateDelegation(id, delegation).toString()));
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
    public ResponseEntity<AppResponse<JsonNode>> deleteDelegation(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            delegationService.deleteDelegation(id);
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

    @Transactional
    @GetMapping("/read/list")
    public ResponseEntity<AppResponse<JsonNode>> readAllDelegation(@RequestHeader("X-Auth-Token") String token,
                                                                   @RequestParam(value = "page") Optional<Integer> page,
                                                                   @RequestParam(value = "size") Optional<Integer> size,
                                                                   @RequestParam(value = "search") Optional<String> search
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Delegation> delegationPage = delegationService.getAllDelegationSearchable(search.orElse(""), page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(delegationPage.getContent().toString()));
                    respBuilder.info("totalCount", delegationPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(delegationService.getAllDelegationSearchable(search.orElse("")).toString()));
                }
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
}
