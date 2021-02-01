package com.asset.appwork.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class Filter {
    String type;
    String table;
    List<LinkedHashMap> aggregations = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    List<String> groupBy = new ArrayList<>();
    List<LinkedHashMap> where = new ArrayList<>();
}