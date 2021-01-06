package com.asset.appwork.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

<<<<<<< HEAD
=======
/**
 * Created by Bassel on 4/1/2021.
 */
>>>>>>> 68ee27eaaeeed0ad82a6f006aff81fc7da3c6caa
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
