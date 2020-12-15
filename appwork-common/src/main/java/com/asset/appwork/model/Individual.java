package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by omaradl on 12/14/2020.
 */
@Entity
@Data
@Table(name = "O2MyCompanyContactTrackerMOD_CT_entity_Individual")
public class Individual {

    @Id
    Long Id;
    String nameArabic;
    String positionArabic;
    String phone;
    String mobile;
    String email;
}