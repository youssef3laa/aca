package com.asset.appwork.model;


import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "AssetGeneralACAACA_Entity_primaryMember")
public class PrimaryMember {


    public static final String TableName = "ACA_Entity_primaryMember";
    @Id
    private long id;
    private String userCN;
    @ManyToOne
    @JoinColumn(name = "incomingRegistrationEntityId")
    IncomingRegistration incomingRegistration;


    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.valueToTree(this);
        ((ObjectNode) tree).remove("incomingRegistration");
        ((ObjectNode) tree).remove("id");
        if (incomingRegistration != null)
            ((ObjectNode) tree).put("incomingRegistrationEntityId", incomingRegistration.getId());
        return SystemUtil.writeObjectIntoString(tree);
    }

}
