package com.asset.appwork.controller;


import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.PrimaryMemberDto;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.PrimaryMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/primaryMember")
public class PrimaryMemberController {

    @Autowired
    TokenService tokenService;
    @Autowired
    PrimaryMemberService primaryMemberService;

    @PostMapping
    public ResponseEntity<AppResponse<PrimaryMemberDto>> createPrimaryMember(@RequestHeader("X-Auth-Token") String token,
                                                                             @RequestBody PrimaryMemberDto primaryMember) {
        AppResponse.ResponseBuilder<PrimaryMemberDto> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            responseBuilder.data(primaryMemberService.createPrimaryMember(account, primaryMember));
        } catch (AppworkException e) {
            e.printStackTrace();
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }

    @GetMapping("default")
    public ResponseEntity<AppResponse<PrimaryMemberDto>> getDefaultPrimaryMember(@RequestHeader("X-Auth-Token") String token,
                                                                                 PrimaryMemberDto primaryMember) {
        AppResponse.ResponseBuilder<PrimaryMemberDto> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            responseBuilder.data(primaryMemberService.findFirstPrimaryMember(primaryMember));
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }
}
