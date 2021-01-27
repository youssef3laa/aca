package com.asset.appwork.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {
    private Date create_date;
    private String description;
    private Date file_create_date;
    private Date file_modify_date;
    private String file_name;
    private Long file_size;
    private String file_type;
    private Long id;
    private String mime_type;
    private Date modify_date;
    private String name;
    private String owner_id;
    private String provider_id;
    private String version_id;
    private String version_number;
    private String version_number_major;
    private String version_number_minor;
    private String version_number_name;
}
