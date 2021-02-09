package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cs.AppworkCSOperations;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.CreateNode;
import com.asset.appwork.dto.Document;
import com.asset.appwork.dto.Memos;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Memorandum;
import com.asset.appwork.platform.soap.memorandumSOAP;
import com.asset.appwork.repository.MemosRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.Docx;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    Docx docx;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createMemorandum(@RequestHeader("X-Auth-Token") String token, @RequestBody() Memos memo) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            File file = docx.exportJsonToDocx(memo);



            HashMap<String, String> values = new HashMap<>();
            for (Map.Entry value : memo.getValues().entrySet()) {
                String tempValue = "<![CDATA[" + value.getValue().toString() + "]]>";
                values.put(value.getKey().toString(), tempValue);
            }
            memo.setValues(values);

            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            CreateNode createNode = new CreateNode();

            createNode.setType(144);
            createNode.setName(file.getName());
            createNode.setFile(new MockMultipartFile("file", new FileInputStream(file)));
            //TODO get Ids from env or get them from request
            createNode.setParent_id(680482L);
            createNode.setCategory_id(717725L);
            LinkedHashMap<String, String> categoryLinkedHashMap = new LinkedHashMap<>();
            categoryLinkedHashMap.put("717725_2", file.getName());
            Document document = appworkCSOperations.uploadNodeAndSetCategory(createNode, new AppworkCSOperations.DocumentQuery(), categoryLinkedHashMap);

            memo.setNodeId(document.getProperties().getId());

            String addRecordToMemorandum = cordysService.sendRequest(account, new memorandumSOAP().createMemorandum(memo));

            String XMLtoJSON = SystemUtil.convertXMLtoJSON(addRecordToMemorandum);
            ObjectMapper objectMapper = new ObjectMapper();
            long id = objectMapper.readTree(XMLtoJSON).get("Body").get("CreateACA_Entity_MemosResponse").get("ACA_Entity_Memos").get("ACA_Entity_Memos-id").get("Id").asLong();

            String addRecordToMemorandumValues = cordysService.sendRequest(account, new memorandumSOAP().createMemoValues(memo, id));
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
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/get/{jsonId}/{requestId}")
    public ResponseEntity<AppResponse<List<Memorandum>>> getMemorandum(@RequestHeader("X-Auth-Token") String token,
                                                                       @PathVariable("jsonId") String jsonId,
                                                                       @PathVariable("requestId") String requestId) {
        AppResponse.ResponseBuilder<List<Memorandum>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            List<Memorandum> s = memosRepository.findByJsonIdAndRequestId(jsonId, requestId);
            if (s.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();
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
