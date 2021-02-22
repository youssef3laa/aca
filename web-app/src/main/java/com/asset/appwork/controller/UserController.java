package com.asset.appwork.controller;

import com.asset.appwork.AppBuilder;
import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.orgchart.UserManagement;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by karim on 10/28/20.
 */
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    TokenService tokenService;
    @Autowired
    UserManagement userManagement;
    @Autowired
    Environment environment;
    @Autowired
    OrgChartService orgChartService;

    @GetMapping("/form/{key}")
    public ResponseEntity<AppResponse<JsonNode>> getForm(@PathVariable("key") String key) {
        String appBuild = environment.getProperty("build.app");
        if (appBuild.equals("true"))  new AppBuilder(environment.getProperty("form.config")).run(key);

        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();

        String rootStr = environment.getProperty("form.config");

        try (FileReader fileReader = new FileReader(new File(rootStr +File.separator+ "output"+File.separator + key + ".json"))) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode outputData = mapper.readTree(fileReader);

            System.out.println(outputData);
            return responseBuilder.data(outputData).status(ResponseCode.SUCCESS).build().getResponseEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBuilder.build().getResponseEntity();
    }

    @GetMapping("/view")
    public ResponseEntity<AppResponse<JsonNode>> getView(@RequestParam("key") String key) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();

        String rootStr = environment.getProperty("form.config");

        String[] paths = key.split("\\|");
        StringBuilder path = new StringBuilder();
        if(paths.length > 0){
            path = new StringBuilder(paths[0]);
            for(int i =1 ; i < paths.length ; i++) path.append(File.separator).append(paths[i]);
        }

        try (FileReader fileReader = new FileReader(rootStr +File.separator+ "views" + File.separator + path + ".json")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode outputData = mapper.readTree(fileReader);

            System.out.println(outputData);
            return responseBuilder.data(outputData).status(ResponseCode.SUCCESS).build().getResponseEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBuilder.build().getResponseEntity();
    }
    @PostMapping("/login")
    public ResponseEntity<AppResponse<String>> login(@RequestBody Account account) {

        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
             userManagement.create(account);
            respBuilder.data(tokenService.generate(account));
        } catch (JsonProcessingException e) {
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            respBuilder.status(e.getCode());
        } catch (IOException e) {
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/details")
    public ResponseEntity<AppResponse<JsonNode>> getLoggedInUserDetails(@RequestHeader("X-Auth-Token") String token)
    {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUserByUsername(account.getUsername()).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }
}
