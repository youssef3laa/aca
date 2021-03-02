package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "AssetGeneralACAACA_Entity_Outcoming_Data")
public class Outcoming {
    @JsonIgnore
    public final static String TABLE = "ACA_Entity_Outcoming_Data";

    @Id
    Long Id;

    String outcomingNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date outcomingDate;

    String outcomingType;
    String attachmentsDescription;
    String numberOfAttachments;
    String notes;
    String outcomingIssueType;

    String recipientName;
    String job;
    String receivingAdministration;

    String incomingIds;


    public String toString(){
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null","");
    }
}
