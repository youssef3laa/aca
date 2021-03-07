package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.SignatureDto;
import com.asset.appwork.dto.SignatureReadDto;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Signature;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.SignatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/signature")
@Slf4j
@RestController
public class SignatureController {

    @Autowired
    TokenService tokenService;

    @Autowired
    SignatureService signatureService;

    @PostMapping
    public ResponseEntity<AppResponse<String>> createSignature(@RequestHeader("X-Auth-Token") String token, SignatureDto signature) {
        AppResponse.ResponseBuilder<String> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            signatureService.createSignature(account, signature);
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (IOException e) {
            log.error(e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

        return responseBuilder.build().getResponseEntity();
    }

    @GetMapping("/all/{incomingEntityId}")
    public ResponseEntity<AppResponse<List<Signature>>> readAllSignatures(@RequestHeader("X-Auth-Token") String token, @PathVariable long incomingEntityId) {
        AppResponse.ResponseBuilder<List<Signature>> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            responseBuilder.data(signatureService.getAllSignaturesByIncomingEntityId(incomingEntityId));

        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        }

        return responseBuilder.build().getResponseEntity();
    }

    @GetMapping("/{signatureEntityId}")
    public ResponseEntity<AppResponse<SignatureReadDto>> readSignature(@RequestHeader("X-Auth-Token") String token, @PathVariable long signatureEntityId) {
        AppResponse.ResponseBuilder<SignatureReadDto> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            responseBuilder.data(signatureService.getSignatureData(signatureEntityId));

        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return responseBuilder.build().getResponseEntity();
    }
}
