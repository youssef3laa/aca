package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.JoinColumn;
import javax.validation.constraints.Null;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralProcessModel {
    String summary;
    String writingDate;
    String workType;
    String incomingMeans;
    String incomingUnit;
    String agency;
    String sector;
    String office;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this);
    }
}
