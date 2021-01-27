package com.asset.appwork.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UploadNodeAndSetCategory {

    private Long parent_id;
    private Long category_id;
    private Integer type;
    private String name;
    private MultipartFile file;
    private String categoriesMap;

}
