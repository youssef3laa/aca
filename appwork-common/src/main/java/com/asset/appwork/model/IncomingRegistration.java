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
    //    String transferTo;
    String confidentiality;
    String incomingType;
    String jobType;
    String priorityLevel;
    String taskType;
    Long outcomingId;

    @Transient
    Long requestEntityId;

    @OneToOne
    @JoinColumn(name = "requestEntityId")
    RequestEntity requestEntity;

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.valueToTree(this);
        ((ObjectNode) tree).remove("requestEntity");
        ((ObjectNode) tree).remove("id");
        if (requestEntity != null)
            ((ObjectNode) tree).put("requestEntityId", requestEntity.getId());
        return SystemUtil.writeObjectIntoString(tree);
    }
}
