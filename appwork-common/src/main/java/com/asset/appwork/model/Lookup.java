package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_lookup")
public class Lookup {
    @Id
    Long Id;

    String category;
    @Column(name = "[key]")
    String key;
    String arValue;
    String enValue;
    Long parentId;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null","");
    }
}
