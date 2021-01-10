package com.asset.appwork.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Memos {
    String requestId;
    String jsonId;
    HashMap<String, String> values;

    public Memos(String requestId, String jsonId) {
        this.requestId = requestId;
        this.jsonId = jsonId;
    }

    public HashMap<String, String> getValues() { return values; }

    public void setValues(HashMap<String, String> values) { this.values = values; }

}
