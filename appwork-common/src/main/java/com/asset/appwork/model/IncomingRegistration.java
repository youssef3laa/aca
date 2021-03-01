package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "AssetGeneralACAACA_Entity_Incoming_Registration")
public class IncomingRegistration {
    @JsonIgnore
    @Transient
    public static String table = "ACA_Entity_Incoming_Registration";

    @Id
    Long id;

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
    String responsibleEntityEdara;
    String responsibleEntityGehaz;
    String responsibleEntityketa3;
    String topicSummary;
    String confidentiality;
    String incomingType;
    String jobType;
    String priorityLevel;
    String taskType;
    Long outcomingId;

    Long requestEntityId;


    @Transient

    String confidentialityTxt;
    @Transient
    String incomingTypeTxt;
    @Transient
    String jobTypeTxt;
    @Transient
    String priorityLevelTxt;
    @Transient
    String taskTypeTxt;
    @Transient
    String incomingFromTxt;
    @Transient
    String responsibleEntityGehazTxt;
    @Transient
    String responsibleEntityKeta3Txt;
    @Transient
    String responsibleEntityEdaraTxt;



    @OneToOne
    @JoinColumn(name = "requestEntityId",insertable = false,updatable = false)
    RequestEntity requestEntity;

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.valueToTree(this);
        ((ObjectNode) tree).remove("requestEntity");
        ((ObjectNode) tree).remove("id");
        ((ObjectNode) tree).remove("confidentialityTxt");
        ((ObjectNode) tree).remove("incomingTypeTxt");
        ((ObjectNode) tree).remove("jobTypeTxt");
        ((ObjectNode) tree).remove("priorityLevelTxt");
        ((ObjectNode) tree).remove("taskTypeTxt");
        ((ObjectNode) tree).remove("incomingFromTxt");
        ((ObjectNode) tree).remove("responsibleEntityGehazTxt");
        ((ObjectNode) tree).remove("responsibleEntityKeta3Txt");
        ((ObjectNode) tree).remove("responsibleEntityEdaraTxt");
        if (requestEntity != null)
            ((ObjectNode) tree).put("requestEntityId", requestEntity.getId());
        return SystemUtil.writeObjectIntoString(tree);
    }
}
