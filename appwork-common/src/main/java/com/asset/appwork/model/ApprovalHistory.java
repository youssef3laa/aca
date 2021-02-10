package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    String comment;
    String userCN;
    String entityId;
    String processName;
    String stepId;
    String parent;
    @Transient
    String displayName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd , hh:mm:ss a")
    Date approvalDate;
}
