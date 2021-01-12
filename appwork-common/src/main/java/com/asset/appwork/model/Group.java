package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity(name = "Group")
@Table(name = "O9OpenTextEntityIdentityComponentsIdentity")
public class Group extends BaseIdentity<Group> {
    @Column(name = "GroupCode")
    String groupCode;
    @Column(name = "Head")
    Boolean isHeadRole;
    @Column(name = "Vice")
    Boolean isViceRole;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "Unit_Id",
            referencedColumnName = "Id"
    )
    @JsonProperty("unit")
    Unit unit;
    @Transient
    String cn;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Boolean getHeadRole() {
        return isHeadRole;
    }

    public void setHeadRole(Boolean headRole) {
        isHeadRole = headRole;
    }

    public Boolean getViceRole() {
        return isViceRole;
    }

    public void setViceRole(Boolean viceRole) {
        isViceRole = viceRole;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getCN() {
        return "cn=" + this.name + ",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=appworks-aca.local";
    }
}
