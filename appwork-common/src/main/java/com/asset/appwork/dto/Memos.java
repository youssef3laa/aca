package com.asset.appwork.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Bassel on 4/1/2021.
 */
@Data
public class Memos {
    String requestId;
    String jsonId;
    String key;
    List<String> notes;

    public Memos(String requestId, String jsonId, String key, List notes) {
        this.requestId = requestId;
        this.jsonId = jsonId;
        this.key = key;
        this.notes = notes;
    }
}
