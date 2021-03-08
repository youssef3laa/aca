package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@DynamicUpdate
@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_signature")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Signature {

    public static final String TABLE = "ACA_Entity_signature";

    @Id
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date signatureDate;


    private Long incomingEntityId;
    private String signatureHeadImgPath;
    private String signatureHeadTxt;
    private String signatureViceImgPath;
    private String signatureViceTxt;


    @NonNull
    @ManyToOne
    @JoinColumn(name = "incomingEntityId", nullable = false, insertable = false, updatable = false)
    private IncomingRegistration incomingRegistration;

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.valueToTree(this);
        ((ObjectNode) tree).remove("id");
        ((ObjectNode) tree).remove("incomingRegistration");
        return SystemUtil.writeObjectIntoString(tree);
    }

}
