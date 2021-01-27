package com.asset.appwork.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentProperties {
    private Long id;
    private Date modify_date;
    private Long modify_user_id;
    private String name;
    private Long parent_id;
    private Long size;
    private String size_formatted;
    private Integer type;
    private String type_name;
    private String versionable;
    private Long volume_id;
}
