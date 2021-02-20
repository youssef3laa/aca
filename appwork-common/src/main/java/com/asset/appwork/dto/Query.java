package com.asset.appwork.dto;

import lombok.Data;
import org.xlsx4j.sml.Col;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class Query {
    String table;
    List<Join> joins = new ArrayList<>();
    List<Aggregate> aggregations = new ArrayList<>();
    List<SortBy> sortBy = new ArrayList<>();
    List<Column> columns = new ArrayList<>();
    List<Column> groupBy = new ArrayList<>();
    List<Condition> where = new ArrayList<>();
    Page page;

    @Data
    public static class Page {
        int number;
        int size;
    }

    @Data
    public static class Column {
        String table;
        String name;
    }

    @Data
    public static class Aggregate {
        String function;
        Column column;
    }

    @Data
    public static class SortBy {
        Column column;
        String direction;
    }

    @Data
    public static class Condition {
        List<Condition> or;
        List<Condition> and;
        String type;
        Column column;
        Column column2;
        Object value;
    }

    @Data
    public static class Join {
        String joinable;
        String type;
        List<Condition> on;
    }
}