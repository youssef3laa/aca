package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "AssetGeneralACAACA_Entity_linkIncoming")
public class LinkIncoming {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String sourceIncomingId;
    private String targetIncomingId;
    private String requestEntityId;
    private String sourceRequestId;
    private String targetRequestId;

    public String toString() {
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null", "");
    }
}
