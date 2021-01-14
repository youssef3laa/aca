package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import lombok.Data;

@Data
public class GeneralProcessModel {
    String summary;
    String writingDate;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this);
    }
}
