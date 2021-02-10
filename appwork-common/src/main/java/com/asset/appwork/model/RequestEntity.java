package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_request")
public class RequestEntity {
    @Id
    @JsonIgnore
    Long Id;

    String entityName;
    String entityId;
    String process;
    Date date;
    String status;
    String initiator;
    String subject;
    String requestNumber;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this);
    }
}
