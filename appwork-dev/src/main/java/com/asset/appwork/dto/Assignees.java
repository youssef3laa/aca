package com.asset.appwork.dto;

import lombok.Data;

import java.util.List;

@Data
public class Assignees {
    List<String> assignee;
    String stepId, page;
    Router router;
}
