package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_request")
public class RequestEntity {
    @Id
    Long Id;

    String entityName;
    String entityId;
    String process;

    @Column(name = "requestDate")
    @JsonAlias("requestDate")
    @JsonProperty("requestDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    Date date;
    String status;
    String initiator;
    String subject;
    String requestNumber;
    String processInstanceId;

    public String toString() {
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null", "");
    }
}
