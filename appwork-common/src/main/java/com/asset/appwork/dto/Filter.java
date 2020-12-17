package com.asset.appwork.dto;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class Filter {
    String type;
    String table;
    List<LinkedHashMap> aggregations;
    List<String> columns;
    List<String> groupBy;
    List<LinkedHashMap> where;

    public Filter(){}

    public Filter(String queryJson) throws AppworkException{
        try{
            this.table = SystemUtil.readJSONField(queryJson, "table");
            this.aggregations = (List<LinkedHashMap>) SystemUtil.readJSONArray(queryJson, "aggregations");
            this.columns = (List<String>) SystemUtil.readJSONArray(queryJson, "columns");
            this.groupBy = (List<String>) SystemUtil.readJSONArray(queryJson, "groupBy");
            this.where = (List<LinkedHashMap>) SystemUtil.readJSONArray(queryJson,"where");
        } catch (JsonProcessingException e){
            throw new AppworkException("", ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Filter(String table, List<LinkedHashMap> aggregations, List<String> columns, List<String> groupBy, List<LinkedHashMap> where) {
        this.table = table;
        this.aggregations = aggregations;
        this.columns = columns;
        this.groupBy = groupBy;
        this.where = where;
    }

    public List<LinkedHashMap> getAggregations() {
        return (aggregations == null)? new ArrayList<>() : aggregations;
    }

    public List<String> getColumns() {
        return (columns == null)? new ArrayList<>() : columns;
    }

    public List<String> getGroupBy() {
        return (groupBy == null)? new ArrayList<>() : groupBy;
    }

    public List<LinkedHashMap> getWhere() {
        return (where == null)? new ArrayList<>() : where;
    }
}
