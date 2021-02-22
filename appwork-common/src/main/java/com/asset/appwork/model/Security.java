package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_Security")
public class Security {
    @Id
    Long Id;

    Integer type;
    String target;
    String config;

    public String toString() {
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null", "");
    }
}
