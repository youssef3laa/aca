package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    ApprovalHistoryRepository historyRepository;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createHistory(@RequestHeader("X-Auth-Token") String token,@RequestBody() ApprovalHistory approvalHistory){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) throw new AppworkException("", ResponseCode.UNAUTHORIZED);

            String response = cordysService.sendRequest(account, new ApprovalHistorySoap.createHistory(approvalHistory));
            respBuilder.data(response);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("/{entityId}/{processType}")
    public ResponseEntity<AppResponse<List<ApprovalHistory>>> readHistory(@RequestHeader("X-Auth-Token") String token,
                                                                          @PathVariable("entityId") String entityId,
                                                                          @PathVariable("processName") String processName) {
        AppResponse.ResponseBuilder<List<ApprovalHistory>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            if (account == null) throw new AppworkException("", ResponseCode.UNAUTHORIZED);

            Optional<List<ApprovalHistory>> histories = historyRepository.findByProcessNameAndEntityId(processName, entityId);
            if(!histories.isPresent()) throw new AppworkException("", ResponseCode.NO_CONTENT);
            respBuilder.data(histories.get());

        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }
}
