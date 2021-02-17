package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_lookup")
public class Lookup {
    @Id
    Long Id;
    String stringKey;
    String category;
    String key;
    String arValue;
    String enValue;
    Long parentId;
    String stringKey;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null","");
    }
}
