package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.IncomingRegistration;
import com.asset.appwork.model.Inquiry;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.IncomingInquiryService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/incoming/inquiry")
@Slf4j
public class IncomingInquiryController {


    @Autowired
    IncomingInquiryService incomingInquiryService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/incomingRegistration/{incomingRegistrationId}")
    public ResponseEntity<AppResponse<Inquiry>> createInquiry(@RequestHeader("X-Auth-Token") String token,
                                                              @RequestBody Inquiry inquiry,
                                                              @PathVariable("incomingRegistrationId") Long inquiryEntityId) {

        AppResponse.ResponseBuilder<Inquiry> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.INVALID_TOKEN);

            IncomingRegistration incomingRegistration = new IncomingRegistration();
            incomingRegistration.setId(inquiryEntityId);
            inquiry.setIncomingRegistration(incomingRegistration);
            responseBuilder.data(incomingInquiryService.createInquiry(account, inquiry));
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }


    @GetMapping("/all/incomingRegistration/{incomingRegistrationId}")
    public ResponseEntity<AppResponse<Page<Inquiry>>> getInquiriesByIncomingRegistrationId(@RequestHeader("X-Auth-Token") String token,
                                                                                           @NonNull @PathVariable("incomingRegistrationId") Long incomingRegistrationId,
                                                                                           @RequestParam(value = "page") Optional<Integer> page,
                                                                                           @RequestParam(value = "size") Optional<Integer> size,
                                                                                           @RequestParam(value = "search") Optional<String> search) {

        AppResponse.ResponseBuilder<Page<Inquiry>> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.INVALID_TOKEN);
            Page<Inquiry> allInquiriesByIncomingId = incomingInquiryService.getAllInquiriesByIncomingId(incomingRegistrationId, page, size, search);
            responseBuilder.data(allInquiriesByIncomingId);
            responseBuilder.info("totalCount", allInquiriesByIncomingId.getTotalElements());
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }

    @DeleteMapping("one/{inquiryEntityId}")
    public ResponseEntity<AppResponse<List<Inquiry>>> deleteOneInquiry(@RequestHeader("X-Auth-Token") String token,
                                                                       @NonNull @PathVariable("inquiryEntityId") Long inquiryEntityId) {

        AppResponse.ResponseBuilder<List<Inquiry>> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.INVALID_TOKEN);
            incomingInquiryService.deleteInquiry(account, inquiryEntityId);
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }

    @PutMapping()
    public ResponseEntity<AppResponse<Inquiry>> updateInquiry(@RequestHeader("X-Auth-Token") String token,
                                                              @NonNull @RequestBody Inquiry inquiry) {

        AppResponse.ResponseBuilder<Inquiry> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.INVALID_TOKEN);
            responseBuilder.data(incomingInquiryService.updateInquiry(inquiry));
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }
}
