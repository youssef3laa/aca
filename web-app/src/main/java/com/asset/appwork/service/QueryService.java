package com.asset.appwork.service;

import com.asset.appwork.QueryBuilder;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.Query;
import com.asset.appwork.dto.QueryResult;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class QueryService {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;
    @Autowired
    EntityManager entityManager;
    @Autowired
    OrgChartService orgChartService;

    private static String requestEntity = "RequestEntity";

    @Transactional
    public QueryResult runQuery(Query query, Account account) throws AppworkException {
        QueryBuilder queryBuilder = new QueryBuilder(entityManager);

        if(query.getTable().equals(requestEntity)) {
            User user = orgChartService.getLoggedInUser(account);
            Optional<Group> group = user.getGroup().stream().findFirst();
            Query.Column column = new Query.Column();
            column.setTable("RequestEntity");
            column.setName("workingRoles");
            Query.Condition condition = new Query.Condition();
            if(group.isPresent()) {
                condition.setType("like");
                condition.setColumn(column);
                condition.setValue("%;"+group.get().getGroupCode()+";%");
            }else{
                condition.setType("equal");
                condition.setColumn(column);
                condition.setValue("-1");
            }
            query.getWhere().add(condition);
        }

        QueryResult queryResult = queryBuilder.runQuery(query);

        if(query.getColumns().size() < 1 && query.getAggregations().size() < 1 && query.getJoins().size() < 1){
            try{
                Class<?> serviceClass = Class.forName("com.asset.appwork.service."+ query.getTable() +"Service");
                GenericService genericService = (GenericService) beanFactory.createBean(serviceClass);
                queryResult.setContent(genericService.updateResult(queryResult.getContent()));
            }catch (ClassNotFoundException e){
                return queryResult;
            }
        }

        return queryResult;
    }

    public JsonNode getQueryDataForCharts(Query key) throws AppworkException {
        QueryBuilder queryBuilder = new QueryBuilder(entityManager);
        try {
            List<LinkedHashMap> list = queryBuilder.runQuery(key).getContent();
            List<Object> data = new ArrayList<>();
            List<Object> labels = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode output = mapper.createObjectNode();
            List<LinkedHashMap> listClone = new ArrayList<>(list);

            listClone.stream().forEach(x -> {
                x.forEach((k, v) -> {
                    if (Objects.isNull(v)) {
                        list.remove(x);
                    }
                });
            });

            list.stream().forEach(x -> {
                x.forEach((k, v) -> {

                    if (v instanceof Long) {
                        data.add(x.get(k));
                    } else if (v instanceof String) {
                        labels.add(x.get(k));
                    }
                });
            });
            output.putPOJO("data", data);
            output.putPOJO("labels", labels);
            return output;
        } catch (AppworkException e) {
            e.printStackTrace();
            throw new AppworkException(e.getMessage(), ResponseCode.QUERY_BUILDER_FAILURE);
        }
    }
}
