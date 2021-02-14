package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name= "AssetGeneralACAACA_Entity_Incoming_Registration")
public class IncomingRegistration {
    @JsonIgnore
    public static String table = "ACA_Entity_Incoming_Registration";

    @Id
    Long Id;

    String subject;
    String constraint;
    String correspondenceNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date incomingDate;
    String incomingFrom;
    String incomingNumber;
    String jobEntityId;
    String attachmentsDescription;
    String numberOfAttachments;
    String registrationNumber;
    String topicSummary;
    String transferTo;
    String confidentiality;
    String incomingType;
    String jobType;
    String priorityLevel;
    String taskType;
    Long outcomingId;

    public String toString(){
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null","");
    }
}
