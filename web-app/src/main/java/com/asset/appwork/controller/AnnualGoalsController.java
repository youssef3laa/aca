package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.AnnualGoals;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.AnnualGoalsRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/goals")

@RestController
public class AnnualGoalsController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    Environment environment;
    @Autowired
    AnnualGoalsRepository annualGoalsRepository;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createAnnualGoal(@RequestHeader("X-Auth-Token") String token, @RequestBody() AnnualGoals annualGoals){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();

        try{
            Account account = tokenService.get(token);

            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
            Entity entity = new Entity(account,restAPIBaseUrl,"ACA_Entity_AnnualGoals");
            Long entityId = entity.create(annualGoals);
            respBuilder.data(entityId.toString());
        }catch (AppworkException e){
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/get/all")
    public ResponseEntity<AppResponse<List<AnnualGoals>>> getAnnualGoals(@RequestHeader("X-Auth-Token") String token,
                                                                         @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", required = false, defaultValue = ""+10+"" ) int size
                                                               ){
        AppResponse.ResponseBuilder<List<AnnualGoals>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            Page<AnnualGoals> annualGoalsList = annualGoalsRepository.findAll(PageRequest.of(page, size));

            if(annualGoalsList.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();
            respBuilder.data(annualGoalsList.getContent());
            respBuilder.info("totalCount", annualGoalsList.getTotalElements());

        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();

    }
    @GetMapping("/get/years")
    public ResponseEntity<AppResponse<List<Integer>>> getYears(@RequestHeader("X-Auth-Token") String token){
        AppResponse.ResponseBuilder<List<Integer>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            List<Integer> yearsList = annualGoalsRepository.getAllYears();

            if(yearsList.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();
            respBuilder.data(yearsList);

        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }
    @GetMapping("/get/year")
    public ResponseEntity<AppResponse<List<AnnualGoals>>> getAnnualGoalsByYear(@RequestHeader("X-Auth-Token") String token,
                                                                         @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", required = false, defaultValue = ""+10+"" ) int size,
                                                                         @RequestParam Integer year
    ){
        AppResponse.ResponseBuilder<List<AnnualGoals>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            Page<AnnualGoals> annualGoalsList = annualGoalsRepository.getAnnualGoalsByYearEquals(year,PageRequest.of(page, size));

            if(annualGoalsList.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();
            respBuilder.data(annualGoalsList.getContent());
            respBuilder.info("totalCount", annualGoalsList.getTotalElements());

        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();

    }
}
