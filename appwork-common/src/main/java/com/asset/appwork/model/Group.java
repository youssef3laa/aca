package com.asset.appwork.model;

import com.asset.appwork.enums.GroupType;
import com.asset.appwork.mixin.GroupPlatformMixIn;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.sql.ResultSet;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "Group.getGroupChildrenRecursivelyFilteredByUnitTypeCode",
                procedureName = "APPWORKSDB.ACA_ORG_SP_getGroupChildrenRecursivelyFilteredByUnitTypeCode",
                resultClasses = Group.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "groupCode_param"),
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "unitTypeCode_param"),
                        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = ResultSet.class, name = "output_cursor")
                }
        ),
})
@Entity(name = "Group")
@DiscriminatorValue("Group")
//@Table(name = "OpenTextEntityIdentityComponentsIdentity")
public class Group extends BaseIdentity {
    @Column(name = "GroupCode")
    String groupCode;
    @Column(name = "Type")
    @Enumerated(EnumType.STRING)
    GroupType type;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "Unit_Id",
            referencedColumnName = "Id"
    )
    @JsonProperty("unit")
    Unit unit;
    @Transient
    String cn;

    public Group() {}

    public Group(BaseIdentity identity) {
        super(identity);
    }

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

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getCn() {
        return "cn=" + this.name + ",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
    }
}
