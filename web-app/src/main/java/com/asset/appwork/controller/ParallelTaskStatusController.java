package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.ParallelTaskStatusRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.OrgChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/parallel")
@RestController
public class ParallelTaskStatusController {

    @Autowired
    TokenService tokenService;
    @Autowired
    OrgChartService orgChartService;
    @Autowired
    ParallelTaskStatusRepository parallelTaskStatusRepository;

    @Transactional
    @GetMapping("/finished/{requestId}")
    public ResponseEntity<AppResponse<Boolean>> isParallelFinished(@RequestHeader("X-Auth-Token") String token,
                                                        @PathVariable("requestId") String requestId) {

        AppResponse.ResponseBuilder<Boolean> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            Boolean finished = true;

            User user = orgChartService.getUserByUsername(account.getUsername());
            Optional<Group> group = user.getGroup().stream().findFirst();
            if(!group.isEmpty()){
                Integer count = parallelTaskStatusRepository.countDistinctByRequestIdAndOwnerAndStatus(requestId, group.get().getCn(), 0);
                if(count > 0){
                    finished = false;
                }
            }
            return responseBuilder.data(finished).status(ResponseCode.SUCCESS).build().getResponseEntity();
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }
}
