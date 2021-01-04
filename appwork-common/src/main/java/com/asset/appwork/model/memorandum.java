package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Bassel on 3/1/2020.
 */
@Entity
@Data
@Table(name = "O9AssetGeneralACAACA_Entity_Memos")
public class memorandum {
    @Id
    Long id;
    String requestId;
    String jsonId;
}