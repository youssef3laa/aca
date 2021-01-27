package com.asset.appwork.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {
    private DocumentProperties properties;
    private List<Version> versions;
    private List<LinkedHashMap<String, String>> categories;

//    @SuppressWarnings("unchecked")
//    @JsonProperty("properties")
//    @JsonAlias("properties")
//    private void unpackNestedProperties( LinkedHashMap<String,Object> properties) {
//        System.out.println(properties);
//            this.setId(properties.get("id"))
//            this.setModify_date(properties.get("id"));
//            this.setModify_user_id(properties.get("id"));
//            this.setName(properties.get("id"));
//            this.setParent_id(properties.get("id"));
//            this.setSize(properties.get("id"));
//            this.setSize_formatted(properties.get("id"));
//            this.setType(properties.get("id"));
//            this.setType_name(properties.get("id"));
//            this.setVersionable(properties.get("id"));
//            this.setVolume_id();
//    }

}
