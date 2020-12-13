package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cs.AppworkCSOperations;
import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.otds.enums.ResponseCode;
import com.asset.appwork.response.AppResponse;
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
                    appworkCSOperations.uploadDocument(mf.getBytes(), parentId, mf.getOriginalFilename(), String.valueOf(144));
                }
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

}
