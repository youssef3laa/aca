package com.asset.appwork.controller;

/**
 * Created by omaradl on 12/14/2020.
 */

import com.asset.appwork.QueryBuilder;
import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.Query;
import com.asset.appwork.dto.QueryResult;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.GenericService;
import com.asset.appwork.service.QueryService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/dynamic/report")
@RestController
public class DynamicReportController {

    @Autowired
    TokenService tokenService;
    @Autowired
    QueryService queryService;

    @PostMapping("/run")
    public ResponseEntity<AppResponse<List>> getForm(@RequestHeader("X-Auth-Token") String token,
                                                     @RequestBody() Query query) {
        AppResponse.ResponseBuilder<List> responseBuilder = AppResponse.builder();
        try{
            Account account = tokenService.get(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            QueryResult queryResult = queryService.runQuery(query,account);
            if(queryResult.getContent().isEmpty()) throw new AppworkException(ResponseCode.NO_CONTENT);
            responseBuilder.info("totalCount", queryResult.getTotalCount());
            responseBuilder.data(queryResult.getContent());
        }catch (AppworkException e){
            e.printStackTrace();
            responseBuilder.status(e.getCode());
        }
        return responseBuilder.build().getResponseEntity();
    }


    @PostMapping("/get")
    public ResponseEntity<AppResponse<JsonNode>> getChartData(@RequestBody() Query key) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try{
//            QueryBuilder queryBuilder = new QueryBuilder(entityManager);
//            List<?> list = queryBuilder.runQuery(key);
            JsonNode list = queryService.getQueryDataForCharts(key);
            String resultString = SystemUtil.writeObjectIntoString(list);
            responseBuilder.status(ResponseCode.SUCCESS);
            return responseBuilder.data(SystemUtil.convertStringToJsonNode(resultString)).build().getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            responseBuilder.status(ResponseCode.QUERY_BUILDER_FAILURE);
        }
        return responseBuilder.build().getResponseEntity();
    }
}
