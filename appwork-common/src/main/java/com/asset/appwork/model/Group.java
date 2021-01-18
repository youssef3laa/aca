package com.asset.appwork.model;

import com.asset.appwork.mixin.GroupPlatformMixIn;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

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

    @SneakyThrows
    public static Group fromString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Group.class, GroupPlatformMixIn.class);
        return mapper.readValue(json, Group.class);
    }

    @SneakyThrows
    public String toPlatformString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(this.getClass(), GroupPlatformMixIn.class);
        return mapper.writeValueAsString(this);
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Boolean getIsHeadRole() {
        return isHeadRole;
    }

    public void setIsHeadRole(Boolean headRole) {
        isHeadRole = headRole;
    }

    public Boolean getIsViceRole() {
        return isViceRole;
    }

    public void setIsViceRole(Boolean viceRole) {
        isViceRole = viceRole;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getCn() {
        return "cn=" + this.name + ",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=appworks-aca.local";
    }
}
