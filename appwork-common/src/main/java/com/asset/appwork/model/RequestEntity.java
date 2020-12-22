package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import lombok.Data;

@Data
public class RequestEntity {
    String receiver;
    String notes;
    String requestDate;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this);
    }
}
