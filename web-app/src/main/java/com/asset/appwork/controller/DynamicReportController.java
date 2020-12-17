package com.asset.appwork.controller;

/**
 * Created by omaradl on 12/14/2020.
 */

import com.asset.appwork.QueryBuilder;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/dynamic/report")
@RestController
public class DynamicReportController {

    @Autowired
    EntityManager entityManager;

    @GetMapping("/get")
    public ResponseEntity<AppResponse<ObjectNode>> getForm(@RequestBody() String key) {
        AppResponse.ResponseBuilder<ObjectNode> responseBuilder = AppResponse.builder();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        try{
            QueryBuilder queryBuilder = new QueryBuilder(entityManager);
            List<?> list = queryBuilder.runQuery(key);

            result.put("Result", SystemUtil.writeObjectIntoString(list));

            responseBuilder.status(ResponseCode.SUCCESS);
            return responseBuilder.data(result).build().getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            result.put("Error",e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.data(result).build().getResponseEntity();
    }
}
