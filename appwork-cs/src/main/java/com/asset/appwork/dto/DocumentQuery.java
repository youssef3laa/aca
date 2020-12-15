package com.asset.appwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DocumentQuery {

    @JsonProperty("where_type")
    String whereType;

    String sort;
    String fields;

}
