package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_Opinion")
public class Opinion {
    @Id
    Long id;

    String opinion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd , hh:mm:ss a")
    Date opinionDate;
    String userCN;
    String requestId;
    String requestNumber;
    @Transient
    String displayName;
    @Transient
    String unitName;
}
