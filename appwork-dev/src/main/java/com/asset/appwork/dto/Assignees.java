package com.asset.appwork.dto;

import lombok.Data;

import java.util.List;

@Data
public class Assignees {
    List<String> assignee;
    String owner;
    String stepId, component, config, readonlyComponent;
    Router router;
}
