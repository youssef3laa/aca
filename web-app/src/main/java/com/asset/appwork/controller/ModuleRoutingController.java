package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.schema.OutputSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/module/routing")

@Slf4j
@RestController
public class ModuleRoutingController {
    @Autowired
    TokenService tokenService;
    @Autowired
    ModuleRouting moduleRouting;

    @Transactional
    @PostMapping("/test")
    public ResponseEntity<AppResponse<String>> complete(@RequestHeader("X-Auth-Token") String token, @RequestBody OutputSchema outputSchema) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            String response = moduleRouting.calculateOutputSchema(outputSchema, account);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }
}
