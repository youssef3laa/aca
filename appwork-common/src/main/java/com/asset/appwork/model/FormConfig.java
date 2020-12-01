package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "O2MyCompanyACAACA_Entity_FormConfig")
public class FormConfig {
    @Id
    long Id;
    @Column(name = "keyStr")
    String key;
    String path;
}
