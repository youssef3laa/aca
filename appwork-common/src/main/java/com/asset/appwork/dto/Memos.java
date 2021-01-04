package com.asset.appwork.dto;

import lombok.Data;

/**
 * Created by karim on 10/29/20.
 */
@Data
public class Memos {
    String requestId;
    String jsonId;
    String memorandumId;
    String key;
    String value;

    public Memos(String requestId, String jsonId, String memorandumId, String key, String value) {
        this.requestId = requestId;
        this.jsonId = jsonId;
        this.memorandumId = memorandumId;
        this.key = key;
        this.value = value;
    }
}
