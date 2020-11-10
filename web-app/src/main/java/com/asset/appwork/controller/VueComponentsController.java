package com.asset.appwork.controller;

import com.asset.appwork.response.AppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by omaradl on 11/10/2020.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/vue/components")
@RestController
public class VueComponentsController {

    @GetMapping("/get")
    public ResponseEntity<AppResponse<String>> getHumanTask(){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        respBuilder.data("FormGenerator.vue");
        return respBuilder.build().getResponseEntity();
    }
}
