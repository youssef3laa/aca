package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Bassel on 29/12/2020.
 */
@Entity
@Data
@Table(name = "AssetGeneralACAACA_Entity_approval_history")
public class ApprovalHistory {
    @Id
    Long id;

    String decision;
    @Column(name = "\"comment\"")
    String comment;
    String userCN;
    String entityId;
    String processName;
    String stepId;
    String parent;
    String readonlyComponent;
    Long requestId;
    String requestNumber;
    String subject;
    String receiverCN;
    @Transient
    String displayName;
    @Transient
    String unitName;
    @Transient
    String receiverDisplayName;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd , hh:mm:ss a")
    Date approvalDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd , hh:mm:ss a")
    Date receiveDate;
}
