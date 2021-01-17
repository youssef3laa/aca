package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.Memos;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Memorandum;
import com.asset.appwork.platform.soap.memorandumSOAP;
import com.asset.appwork.repository.MemosRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by Bassel on 3/1/2021.
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

            String addRecordToMemorandum = cordysService.sendRequest(account, new memorandumSOAP().createMemorandum(memos));

            String XMLtoJSON = SystemUtil.convertXMLtoJSON(addRecordToMemorandum);
            ObjectMapper objectMapper = new ObjectMapper();
                long id = objectMapper.readTree(XMLtoJSON).get("Body").get("CreateACA_Entity_MemosResponse").get("ACA_Entity_Memos").get("ACA_Entity_Memos-id").get("Id").asLong();

            String addRecordToMemorandumValues = cordysService.sendRequest(account, new memorandumSOAP().createMemoValues(memos, id));
            respBuilder.data(addRecordToMemorandumValues);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/get/{jsonId}/{requestId}")
    public ResponseEntity<AppResponse<List<Memorandum>>> getMemorandum(@RequestHeader("X-Auth-Token") String token,
                                                                       @PathVariable("jsonId") String jsonId,
                                                                       @PathVariable("requestId") String requestId){
        AppResponse.ResponseBuilder<List<Memorandum>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            List<Memorandum> s = memosRepository.findByJsonIdAndRequestId(jsonId, requestId);
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
