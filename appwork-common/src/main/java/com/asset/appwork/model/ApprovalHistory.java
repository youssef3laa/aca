package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Bassel on 29/12/2020.
 */
@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_approval_history")
public class ApprovalHistory {
    @Id
    Long id;

    String decision;
    String comment;
    Date approvalDate;
    String userCN;
    String entityId;
    String processName;
    String stepId;
    String parent;
}