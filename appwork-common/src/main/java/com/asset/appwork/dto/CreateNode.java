package com.asset.appwork.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateNode {

    private String parent_id;
    private Integer type;
    private String name;
    private Long original_id;
    private MultipartFile file;
    @JsonRawValue
    private String roles;
}


/*
* Create node of specific type
Create category
required fields: type, parent_id, name
defaults: type = 131
Create channel

required fields: type, parent_id, name
defaults: type = 207
Create document

required fields: type, parent_id, name, file
defaults: type = 144
Create compound document

required fields: type, parent_id, name
defaults: type = 136
Create folder

required fields: type, parent_id, name
defaults: type = 0
Create news

required fields: type, parent_id, name
defaults: type = 208
Create project

required fields: type, parent_id, name
defaults: type = 202
Create shortcut

required fields: type, parent_id, name
defaults: type = 1
Create generation

required fields: type, parent_id, name
defaults: type = 2
Create task

required fields: type, parent_id, name
defaults: type = 206
Create task group

required fields: type, parent_id, name
defaults: type = 205
Create task list

required fields: type, parent_id, name
defaults: type = 204
Create task milestone

required fields: type, parent_id, name
defaults: type = 212
Create URL

required fields: type, parent_id, name
defaults: type = 140
Create virtual folder

required fields: type, parent_id, name
defaults: type = 899
* */