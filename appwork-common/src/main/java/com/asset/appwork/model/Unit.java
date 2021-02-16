package com.asset.appwork.model;

import com.asset.appwork.mixin.UnitPlatformMixIn;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "Unit.getUnitChildrenRecursively",
                procedureName = "APPWORKSDB.ACA_ORG_SP_getUnitChildrenRecursively",
                resultClasses = Unit.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "unitCode_param"),
                        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = ResultSet.class, name = "output_cursor")
                }
        ),
        @NamedStoredProcedureQuery(
                name = "Unit.getUnitChildrenRecursivelyFilteredByUnitTypeCode",
                procedureName = "APPWORKSDB.ACA_ORG_SP_getUnitChildrenRecursivelyFilteredByUnitTypeCode",
                resultClasses = Unit.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "unitCode_param"),
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "unitTypeCode_param"),
                        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = ResultSet.class, name = "output_cursor")
                }
        ),
        @NamedStoredProcedureQuery(
                name = "Unit.getUnitParentsRecursivelyFilteredByUnitTypeCode",
                procedureName = "APPWORKSDB.ACA_ORG_SP_getUnitParentsRecursivelyFilteredByUnitTypeCode",
                resultClasses = Unit.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "unitCode_param"),
                        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "unitTypeCode_param"),
                        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = ResultSet.class, name = "output_cursor")
                }
        ),
})
@Entity(name = "Unit")
@DiscriminatorValue("Unit")
//@Table(name = "OpenTextEntityIdentityComponentsIdentity")
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id", scope = Unit.class)
public class Unit extends BaseIdentity<Unit> {
    @Column(name = "UnitTypeCode")
    String unitTypeCode;
    @Column(name = "UnitCode")
    String unitCode;
    @OneToMany(mappedBy = "unit", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    Collection<Group> group;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "OpenTextEntityIdentityComponentsOrganizationalUnitOrganizationalUnit",
            joinColumns = {@JoinColumn(name = "OrganizationIdBDBD7DDC32F46FF3")},
            inverseJoinColumns = {@JoinColumn(name = "OrganizationalUnit_Id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"OrganizationIdBDBD7DDC32F46FF3", "OrganizationalUnit_Id"})}
    )
    @JsonIgnore
    Collection<Unit> child = new HashSet<>();
    @OneToMany(mappedBy = "unit", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @JsonProperty("positions")
    @JsonIgnore
    Collection<Position> position;
//    @ManyToMany(mappedBy = "child", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "OpenTextEntityIdentityComponentsOrganizationalUnitOrganizationalUnit",
            joinColumns = {@JoinColumn(name = "OrganizationalUnit_Id")},
            inverseJoinColumns = {@JoinColumn(name = "OrganizationIdBDBD7DDC32F46FF3")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"OrganizationIdBDBD7DDC32F46FF3", "OrganizationalUnit_Id"})}
    )
//    @JsonIgnore
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId=true)
//    @JsonFormat(with = JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
    Collection<Unit> parent = new HashSet<>();

    @SneakyThrows
    public static Unit fromString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Unit.class, UnitPlatformMixIn.class);
        return mapper.readValue(json, Unit.class);
    }

    @SneakyThrows
    public String toPlatformString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(this.getClass(), UnitPlatformMixIn.class);
        return mapper.writeValueAsString(this);
    }

    public String getUnitTypeCode() {
        return unitTypeCode;
    }

    public void setUnitTypeCode(String groupCode) {
        this.unitTypeCode = groupCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Collection<Group> getGroup() {
        return group;
    }

    public void setGroup(Collection<Group> groups) {
        this.group = groups;
    }

    public Collection<Unit> getChild() {
        return child;
    }

    public void setChild(Collection<Unit> children) {
        this.child = children;
    }

    public Collection<Position> getPosition() {
        return position;
    }

    public void setPosition(Collection<Position> position) {
        this.position = position;
    }

    public Collection<Unit> getParent() {
        return parent;
    }

    public void setParent(Collection<Unit> parent) {
        this.parent = parent;
    }}

//    @JsonProperty(value = "", access = JsonProperty.Access.WRITE_ONLY)
//    @JsonIgnore
//    @Size(max = 0)
//    @BatchSize(size = 0)