package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.dto.Account;
import com.asset.appwork.cs.AppworkCSOperations;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by omaradl on 11/16/2020.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/document")
@RestController
public class DocumentsController {
    @Autowired
    TokenService tokenService;

    @PostMapping("/upload")
    public ResponseEntity<AppResponse<String>> uploadFile(@RequestHeader("X-Auth-Token") String token,
                                                          @RequestParam("file") MultipartFile[] files,
                                                          @RequestParam("parentId") String parentId) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            try {
                for (MultipartFile mf : files) {
                    AppworkCSOperations.CSResponse csResponse = appworkCSOperations.
                            uploadDocument(mf.getBytes(), parentId, mf.getOriginalFilename(), String.valueOf(144));
                    System.out.println(csResponse.getResponse());
                    System.out.println(csResponse.getStatusCode());
                }
                if (files.length != 0)
                    respBuilder.data("file has been uploaded");

            } catch (IOException e) {
                e.printStackTrace();
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            e.printStackTrace();
        }
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
            AppworkCSOperations.CSResponse csResponse = appworkCSOperations.getSubNodes(parentId, documentQuery);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(csResponse.getStatusCode()));
        } catch (AppworkException | JsonProcessingException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            respBuilder.data(obj);
        }
        return respBuilder.build().getResponseEntity();
    }

}
