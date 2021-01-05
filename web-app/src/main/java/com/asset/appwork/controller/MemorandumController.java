package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.Memos;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.model.memoValues;
import com.asset.appwork.model.memorandum;
import com.asset.appwork.platform.soap.memorandumSOAP;
import com.asset.appwork.repository.MemosRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Bassel on 3/1/2020.
 */
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/memorandum")
@RestController
public class MemorandumController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    MemosRepository memosRepository;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createMemorandum(@RequestHeader("X-Auth-Token") String token,@RequestBody() Memos memos){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            String response = cordysService.sendRequest(account, new memorandumSOAP().createMemorandum(memos));
            String response2 = cordysService.sendRequest(account, new memorandumSOAP().createMemoValues(memos));
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

    @GetMapping("/get/{id}")
    public ResponseEntity<AppResponse<List<memorandum>>> getMemorandum(@RequestHeader("X-Auth-Token") String token, @PathVariable("id") String key){
        AppResponse.ResponseBuilder<List<memorandum>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            List<memorandum> s = memosRepository.findAll();
            if(s.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();
            respBuilder.data(s);


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
}
