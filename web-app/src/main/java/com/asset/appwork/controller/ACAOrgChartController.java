package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.GroupType;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.ACAOrgChartService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/aca/org")
@RestController
@Slf4j
public class ACAOrgChartController {

    @Autowired
    TokenService tokenService;

    @Autowired
    ACAOrgChartService orgChartService;

    @Transactional
    @PostMapping("/unit/create")
    public ResponseEntity<AppResponse<JsonNode>> createUnit(@RequestHeader("X-Auth-Token") String token,
                                                            @RequestBody String props,
                                                            @RequestParam(value = "isRoot") Optional<Boolean> isRoot
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                orgChartService.createUnit(account, props, isRoot.orElse(false));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.createUnit(account, props, isRoot.orElse(false)).toString()));
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
    @PutMapping("/unit/rename")
    public ResponseEntity<AppResponse<JsonNode>> renameUnit(@RequestHeader("X-Auth-Token") String token,
                                                            @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                orgChartService.renameUnit(account, props);
//                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.renameUnit(account, props).toString()));
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
    @PutMapping("/unit/changeParent")
    public ResponseEntity<AppResponse<JsonNode>> changeUnitParent(@RequestHeader("X-Auth-Token") String token,
                                                                  @RequestBody String props,
                                                                  @RequestParam(value = "changeTypeCode") Optional<Boolean> changeTypeCode
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                orgChartService.changeUnitParent(account, props, changeTypeCode.orElse(false));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.changeUnitParent(account, props, changeTypeCode.orElse(false)).toString()));
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
    @PutMapping("/user/update")
    public ResponseEntity<AppResponse<JsonNode>> updateUser(@RequestHeader("X-Auth-Token") String token,
                                                            @RequestBody String props,
                                                            @RequestParam(value = "unitChanged") Optional<Boolean> unitChanged,
                                                            @RequestParam(value = "revokeTasks") Optional<Boolean> revokeTasks
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                orgChartService.updateUser(account, props, unitChanged.orElse(false), revokeTasks.orElse(false));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.updateUser(account, props, unitChanged.orElse(false), revokeTasks.orElse(false)).toString()));
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
    @PostMapping("/group/create/unit/{unitCode}/type/{type}")
    public ResponseEntity<AppResponse<JsonNode>> createGroupOfUnit(@RequestHeader("X-Auth-Token") String token,
                                                                   @PathVariable("unitCode") String unitCode,
                                                                   @PathVariable("type") GroupType type
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.createGroupOfUnit(account, unitCode, type).toString()));
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
