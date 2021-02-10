package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "O9AssetGeneralACAACA_Entity_linkIncoming")
public class LinkIncoming {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String sourceIncomingId;
    private String targetIncomingId;
    private String requestEntityId;

}
