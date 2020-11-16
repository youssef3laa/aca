package com.asset.appwork.controller;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.response.AppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Created by omaradl on 11/16/2020.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/document")
@RestController
public class DocumentsController {
    @PostMapping("/upload")
    public ResponseEntity<AppResponse<String>> uploadFile(@RequestHeader("X-Auth-Token") String token, @RequestParam("file") InputStream file, @RequestParam("parentId") String parentId){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
//        } catch (AppworkException e) {
//            e.printStackTrace();
//            respBuilder.status(e.getCode());
//        }
        return respBuilder.build().getResponseEntity();
    }

}
