package com.asset.appwork.service;

import com.asset.appwork.QueryBuilder;
import com.asset.appwork.dto.Filter;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class QueryService {
    @Autowired
        EntityManager entityManager;


        public JsonNode getQueryDataForCharts(Filter key) throws AppworkException {
            QueryBuilder queryBuilder = new QueryBuilder(entityManager);
            try {
                List<LinkedHashMap> list = queryBuilder.runQuery(key);
                List<Object> data = new ArrayList<>();
                List<Object> labels = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode output = mapper.createObjectNode();
                List<LinkedHashMap> listClone = new ArrayList<>(list);

                listClone.stream().forEach(x->{
                   x.forEach((k,v) ->{
                       if(Objects.isNull(v)) {
                           list.remove(x);}
                   });
                });

                list.stream().forEach(x -> {
                    x.forEach((k,v) ->{

                        if (v instanceof Long){
                            data.add(x.get(k));
                        }
                        else if(v instanceof String){
                            labels.add(x.get(k));
                        }
                    });
                });
                output.putPOJO("data",data);
                output.putPOJO("labels",labels);
                return  output ;
            } catch (AppworkException e) {
                e.printStackTrace();
                throw new AppworkException(e.getMessage(), ResponseCode.QUERY_BUILDER_FAILURE);
            }
        }
    }
