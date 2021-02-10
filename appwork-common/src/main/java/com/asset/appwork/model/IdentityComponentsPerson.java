package com.asset.appwork.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by karim on 10/19/20.
 */
//@Table(name = "trades_#{tableNameSuffix}")
@Entity
@Data
@Table(name = "OpenTextEntityIdentityComponentsPerson")
public class IdentityComponentsPerson {
    @Id
    long Id;
    String DisplayName;
}
