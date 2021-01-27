package com.asset.appwork.model;

import com.asset.appwork.mixin.UnitPlatformMixIn;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity(name = "Unit")
@Table(name = "O9OpenTextEntityIdentityComponentsIdentity")
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
            name = "O9OpenTextEntityIdentityComponentsOrganizationalUnitOrganizationalUnit",
            joinColumns = {@JoinColumn(name = "OrganizationIdA7A9FD625B78137F")},
            inverseJoinColumns = {@JoinColumn(name = "OrganizationalUnit_Id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"OrganizationIdA7A9FD625B78137F", "OrganizationalUnit_Id"})}
    )
    @JsonIgnore
    Collection<Unit> child = new HashSet<>();
    @OneToMany(mappedBy = "unit", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @JsonProperty("positions")
    @JsonIgnore
    Collection<Position> position;
    @ManyToMany(mappedBy = "child", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "O9OpenTextEntityIdentityComponentsOrganizationalUnitOrganizationalUnit",
//            joinColumns = {@JoinColumn(name = "OrganizationalUnit_Id")},
//            inverseJoinColumns = {@JoinColumn(name = "OrganizationIdA7A9FD625B78137F")},
//            uniqueConstraints = {@UniqueConstraint(columnNames = {"OrganizationIdA7A9FD625B78137F", "OrganizationalUnit_Id"})}
//    )
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