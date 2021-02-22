package com.asset.appwork.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryResult<T> {
    List<T> content;
    Long totalCount;
}
