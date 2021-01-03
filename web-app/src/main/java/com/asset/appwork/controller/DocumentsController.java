package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cs.AppworkCSOperations;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by omaradl on 11/16/2020.
 */
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/document")
@RestController
public class DocumentsController {
    @Autowired
    TokenService tokenService;

    @PostMapping("/upload")
    public ResponseEntity<AppResponse<JsonNode>> uploadFile(@RequestHeader("X-Auth-Token") String token,
                                                            @RequestParam("file") MultipartFile[] files,
                                                            @RequestParam("parentId") String parentId) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonNodes = objectMapper.createArrayNode();
        String currentFileName = "";
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            try {
                for (MultipartFile mf : files) {
                    currentFileName = mf.getOriginalFilename();
                    Http http = appworkCSOperations.
                            uploadDocument(mf.getBytes(), parentId, mf.getOriginalFilename(), String.valueOf(144));
                    System.out.println(http.getResponse());
                    System.out.println(http.getStatusCode());
                    ObjectNode fileNode = objectMapper.createObjectNode();
                    fileNode.set(mf.getOriginalFilename(), objectMapper.readTree(http.getResponse()));
                    jsonNodes.add(fileNode);

                }
            } catch (IOException e) {
                log.error(e.getMessage());
                respBuilder.status(ResponseCode.UNSUPPORTED_FILE_TYPE);
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.set(currentFileName, objectMapper.readTree(e.getMessage()));
                jsonNodes.add(errorNode);
            }
        } catch (AppworkException | JsonProcessingException e) {
            log.error(e.getMessage());
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put(currentFileName, e.getMessage());
            jsonNodes.add(errorNode);

        }

        respBuilder.data(jsonNodes);
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/list/{parentId}")
    public ResponseEntity<AppResponse<JsonNode>> listSubNodes(@RequestHeader("X-Auth-Token") String token,
                                                              @PathVariable("parentId") String parentId,
                                                              AppworkCSOperations.DocumentQuery documentQuery) {

        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.getSubNodes(parentId, documentQuery);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
            respBuilder.data(SystemUtil.convertStringToJsonNode(http.getResponse()));
        } catch (AppworkException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            respBuilder.data(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return respBuilder.build().getResponseEntity();
    }

    @DeleteMapping("delete/{nodeId}")
    public ResponseEntity<AppResponse<JsonNode>> deleteNode(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("nodeId") String nodeId) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.deleteNode(nodeId);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        } catch (AppworkException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            respBuilder.data(obj);
        }
        return respBuilder.build().getResponseEntity();
    }
}
