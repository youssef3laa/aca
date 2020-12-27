package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "O9AssetOrgACAOrganizationalUnitGroup",
            joinColumns = {@JoinColumn(name = "OrganizationIdB06E0FBB7FCC75BD")},
            inverseJoinColumns = {@JoinColumn(name = "Group_Id")}
    )
    @JsonBackReference
    @JsonProperty("groups")
    Collection<Group> group = new HashSet<>();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "O9OpenTextEntityIdentityComponentsOrganizationalUnitOrganizationalUnit",
            joinColumns = {@JoinColumn(name = "OrganizationalUnit_Id")}, //TODO: Check rows
            inverseJoinColumns = {@JoinColumn(name = "OrganizationIdA7A9FD625B78137F")}) //TODO: Check rows
    @JsonBackReference
    @JsonProperty("parents")
    private Collection<Unit> parent = new HashSet<>();
    @ManyToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonProperty("children")
    private Collection<Unit> child = new HashSet<>();

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
}
