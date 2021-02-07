package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.platform.soap.ApprovalHistorySOAP;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.ApprovalHistoryService;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.service.OrgChartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Bassel on 28/12/2020.
 */
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/history")
@RestController
public class ApprovalHistoryController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;

    @Autowired
    OrgChartService orgChartService;
    //TODO make approval history service and move repository to it
    @Autowired
    ApprovalHistoryService approvalHistoryService;
    @Autowired
    ApprovalHistoryRepository historyRepository;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createHistory(@RequestHeader("X-Auth-Token") String token,@RequestBody() ApprovalHistory approvalHistory){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            String response = cordysService.sendRequest(account, new ApprovalHistorySOAP().createHistoryApproval(approvalHistory));
            respBuilder.data(response);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/{processName}/{entityId}")
    public ResponseEntity<AppResponse<List<ApprovalHistory>>> readHistory(@RequestHeader("X-Auth-Token") String token,
                                                             @PathVariable("entityId") String entityId,
                                                             @PathVariable("processName") String processName,
                                                             @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNumber,
                                                             @RequestParam(value = "size", required = false, defaultValue = ""+1+"" ) Integer pageSize) {
        AppResponse.ResponseBuilder<List<ApprovalHistory>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            Page<ApprovalHistory> histories = historyRepository.findByProcessNameAndEntityIdOrderByApprovalDateDesc(processName, entityId, PageRequest.of(pageNumber, pageSize));
            if(histories.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();

            respBuilder.info("totalCount", histories.getTotalElements());
            respBuilder.data(approvalHistoryService.addDisplayNameToApprovals(histories.getContent()));
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/user")
    public ResponseEntity<AppResponse<List<ApprovalHistory>>> readUserHistory(@RequestHeader("X-Auth-Token") String token,
                                                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNumber,
                                                                          @RequestParam(value = "size", required = false, defaultValue = ""+1+"" ) Integer pageSize){
    AppResponse.ResponseBuilder<List<ApprovalHistory>> responseBuilder = AppResponse.builder();
    try {
        Account account = tokenService.get(token);

        if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

        Page<ApprovalHistory> histories = historyRepository.findByUserCNOrderByApprovalDateDesc(orgChartService.getLoggedInUser(account).getCN(),PageRequest.of(pageNumber,pageSize));

        if (histories.hasContent()) return responseBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();

        responseBuilder.info("totalCount", histories.getTotalElements());
        responseBuilder.data(approvalHistoryService.addDisplayNameToApprovals(histories.getContent()));
    } catch (AppworkException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        responseBuilder.status(e.getCode());
    } catch (Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build().getResponseEntity();
}




}
