package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "AssetGeneralACAACA_Entity_inquiry")
public class Inquiry {

    @Id
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String birthPlace;
    private String candidatePosition;
    private String fullName;
    private String inquiryType;
    private String JobAuthority;
    private String positionAssignedTo;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "incomingEntityId", nullable = false)
    private IncomingRegistration incomingRegistration;

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.valueToTree(this);
        ((ObjectNode) tree).remove("incomingRegistration");
        ((ObjectNode) tree).remove("id");
        if (incomingRegistration != null)
            ((ObjectNode) tree).put("incomingEntityId", incomingRegistration.getId());
        return SystemUtil.writeObjectIntoString(tree);
    }
//    @SneakyThrows
//    public String toString() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.valueToTree(this).
//        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null", "");
//    }

}
