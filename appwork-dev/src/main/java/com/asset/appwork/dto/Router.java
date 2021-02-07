package com.asset.appwork.dto;

import lombok.Data;

import java.util.List;

@Data
public class Router {
    List<String> fields;
    List<String> decisions;
    List<String> receiverTypes;
    String direction;
    String minimumLevel;
}
