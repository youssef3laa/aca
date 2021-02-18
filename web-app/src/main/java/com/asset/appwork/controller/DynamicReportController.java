package com.asset.appwork.controller;

/**
 * Created by omaradl on 12/14/2020.
 */

import com.asset.appwork.QueryBuilder;
import com.asset.appwork.dto.Filter;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.GenericService;
import com.asset.appwork.service.QueryService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/dynamic/report")
@RestController
public class DynamicReportController {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    EntityManager entityManager;
    @Autowired
    QueryService queryService;
    @PostMapping("/run")
    public ResponseEntity<AppResponse<JsonNode>> getForm(@RequestBody() Filter filter) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try{
            QueryBuilder queryBuilder = new QueryBuilder(entityManager);
            List<?> list = queryBuilder.runQuery(filter);

            if(filter.getColumns().size() < 1 && filter.getAggregations().size() < 1){
                try{
                    Class<?> serviceClass = Class.forName("com.asset.appwork.service."+filter.getTable()+"Service");
                    GenericService genericService = (GenericService) beanFactory.createBean(serviceClass);
//                    GenericService genericService = (GenericService)serviceClass.getConstructor().newInstance();
                    list = genericService.updateResult(list);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            String resultString = SystemUtil.writeObjectIntoString(list);
            responseBuilder.status(ResponseCode.SUCCESS);
            return responseBuilder.data(SystemUtil.convertStringToJsonNode(resultString)).build().getResponseEntity();
        }catch (AppworkException e){
            e.printStackTrace();
            responseBuilder.status(e.getCode());
        }catch (JsonProcessingException e){
            e.printStackTrace();
            responseBuilder.status(ResponseCode.QUERY_BUILDER_FAILURE);
        }
        return responseBuilder.build().getResponseEntity();
    }
    @PostMapping("/get")
    public ResponseEntity<AppResponse<JsonNode>> getChartData(@RequestBody() Filter key) {
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
