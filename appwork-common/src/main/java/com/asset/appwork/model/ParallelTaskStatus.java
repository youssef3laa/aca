package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_parallelTaskStatus")
public class ParallelTaskStatus {
    @Id
    Long id;

    String requestId;
    String owner;
    String processName;
    Integer status;
}
