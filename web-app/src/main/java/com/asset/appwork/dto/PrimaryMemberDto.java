package com.asset.appwork.dto;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
public class PrimaryMemberDto {
    private long id;
    private String userCN;
    private long incomingRegistrationEntityId;

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.valueToTree(this);
        ((ObjectNode) tree).remove("id");
        return SystemUtil.writeObjectIntoString(tree);
    }
}
