package com.asset.appwork.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.List;

@Data
public class Router {
    //    List<String> fields;
//    List<String> decisions;
//    List<String> receiverTypes;
//    String direction;
//    String minimumLevel;
    List<Object> fields;
    List<Object> decisions;
    ObjectNode receiver;

}
