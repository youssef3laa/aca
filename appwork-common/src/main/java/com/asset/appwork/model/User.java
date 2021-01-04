package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity(name = "User")
@Table(name = "O9OpenTextEntityIdentityComponentsIdentity")
public class User extends BaseIdentity<User> {
    @Column(name = "UserId")
    String userId;
    @Column(name = "IdentityDisplayName")
    String displayName;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(
            name = "toPerson_Id",
            referencedColumnName = "Id"
    )
    @JsonProperty("details")
    Person person;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "O9OpenTextEntityIdentityComponentsGroupIdentity",
            joinColumns = {@JoinColumn(name = "Identity_Id")},
            inverseJoinColumns = {@JoinColumn(name = "GroupId9C84CA727C3E880E")}
    )
    @JsonProperty("groups")
    Collection<Group> group = new HashSet<>();;

    @Transient
    String cn;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Collection<Group> getGroup() {
        return group;
    }

    public void setGroup(Collection<Group> group) {
        this.group = group;
    }

    public String getCN() { return "cn=" + this.name + ",cn=organizational users,o=aca,cn=cordys,cn=defaultInst,o=appworks-aca.local"; }
}
