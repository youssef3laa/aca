package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.otds.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.FormConfig;
import com.asset.appwork.repository.FormConfigRepository;
import com.asset.appwork.response.AppResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by karim on 10/28/20.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    TokenService tokenService;
    @Autowired
    CordysManagement cordysManagement;

    @Autowired
    FormConfigRepository formConfigRepository;

    @GetMapping("/form/{key}")
    public ResponseEntity<AppResponse<JsonNode>> getForm(@PathVariable("key") String key) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        Optional<FormConfig> formConfigOptional = formConfigRepository.findByKey(key);

        if (!formConfigOptional.isPresent()) return responseBuilder.build().getResponseEntity();

        try (FileReader fileReader = new FileReader(new File(formConfigOptional.get().getPath()))) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode outputData = mapper.readTree(fileReader);
            // mesh same3
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
            CordysManagement.User user = cordysManagement.getUser(account.getUsername().trim(), account.getPassword(), account.getOrganization().trim());
            account.setSAMLart(user.getSAMLart());
            respBuilder.data(tokenService.generate(account));
        } catch (JsonProcessingException e) {
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }
}
