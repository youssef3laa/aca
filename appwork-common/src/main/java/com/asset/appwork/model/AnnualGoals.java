package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_AnnualGoals")
public class AnnualGoals {

    @Id
    private Long Id;
    private String goal;
    private Integer year;
    private Boolean isTraditional;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null","");
    }
}
