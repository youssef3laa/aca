package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @ManyToMany(mappedBy = "child", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Unit> parent = new HashSet<>();
    @OneToMany(mappedBy = "unit", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonProperty("positions")
//    @JsonIgnore
    Collection<Position> position;

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

    public Collection<Unit> getParent() {
        return parent;
    }

    public void setParent(Collection<Unit> parents) {
        this.parent = parents;
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
}

//    @JsonProperty(value = "", access = JsonProperty.Access.WRITE_ONLY)
//    @JsonIgnore
//    @Size(max = 0)
//    @BatchSize(size = 0)