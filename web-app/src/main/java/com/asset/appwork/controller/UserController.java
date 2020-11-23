package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
