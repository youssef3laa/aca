package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.Memos;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.model.Memorandum;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.LookupRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/lookup")

@RestController
public class LookupController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    Environment environment;
    @Autowired
    LookupRepository lookupRepository;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<String>> createLookup(@RequestHeader("X-Auth-Token") String token, @RequestBody() Lookup lookup){
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();

        try {
            Account account = tokenService.get(token);

            String restAPIBaseUrl = SystemUtil.generateRestAPIBaseUrl(environment, "AssetGeneralACA");
            Entity entity = new Entity(account,
                    restAPIBaseUrl
                    , "ACA_Entity_lookup");
            Long entityId = entity.create(lookup);
            respBuilder.data(entityId.toString());
        }catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/get/category/{category}")
    public ResponseEntity<AppResponse<List<Lookup>>> getLookupsByCategory(@RequestHeader("X-Auth-Token") String token,
                                                                       @PathVariable("category") String category){
        AppResponse.ResponseBuilder<List<Lookup>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            List<Lookup> lookups = lookupRepository.findByCategory(category);
            if(lookups.isEmpty()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();
            respBuilder.data(lookups);

        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/get/category/{category}/key/{key}")
    public ResponseEntity<AppResponse<Lookup>> getLookupsByCategoryAndKey(@RequestHeader("X-Auth-Token") String token,
                                                                                @PathVariable("category") String category,
                                                                                @PathVariable("key") String key){
        AppResponse.ResponseBuilder<Lookup> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if(account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            Optional<Lookup> lookup = lookupRepository.findByCategoryAndKey(category, key);
            if(!lookup.isPresent()) return respBuilder.status(ResponseCode.NO_CONTENT).build().getResponseEntity();
            respBuilder.data(lookup.get());

        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }
}
