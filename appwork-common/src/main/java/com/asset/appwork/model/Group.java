package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity(name = "Group")
@Table(name = "O9OpenTextEntityIdentityComponentsIdentity")
public class Group extends BaseIdentity<Group> {
    @Column(name = "GroupCode")
    String groupCode;
    @Column(name = "Head")
    Boolean isHeadRole;
    @Column(name = "Vice")
    Boolean isViceRole;
    @Transient
    String cn;
    @ManyToMany(mappedBy = "group")
    @JsonManagedReference
    @JsonProperty("units")
    private Collection<Unit> unit = new HashSet<>();

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

    public Collection<Unit> getUnit() {
        return unit;
    }

    public void setUnit(Collection<Unit> units) {
        this.unit = units;
    }

    public String getCN() { return "cn="+this.name+",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=appworks-aca.local"; }
}
