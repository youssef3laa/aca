package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.model.Opinion;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.OpinionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/opinion")
@RestController
public class OpinionController {
    @Autowired
    TokenService tokenService;
    @Autowired
    OpinionService opinionService;

    @GetMapping("/{requestId}")
    public ResponseEntity<AppResponse<List<Opinion>>> readHistory(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("requestId") String requestId,
                                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNumber,
                                                                  @RequestParam(value = "size", required = false, defaultValue = ""+1+"" ) Integer pageSize) {
        AppResponse.ResponseBuilder<List<Opinion>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            Page<Opinion> opinions = opinionService.findByRequestId(requestId, PageRequest.of(pageNumber, pageSize));
            if(opinions.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();

            respBuilder.info("totalCount", opinions.getTotalElements());
            respBuilder.data(opinions.getContent());
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }
}
