package com.asset.appwork.model;

import com.asset.appwork.mixin.UserPlatformMixIn;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity(name = "User")
@Table(name = "O9OpenTextEntityIdentityComponentsIdentity")
public class User extends BaseIdentity<User> {
    @Column(name = "UserId")
    String userId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "toPerson_Id",
            referencedColumnName = "Id"
    )
    @JsonProperty("details")
    Person person;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "O9OpenTextEntityIdentityComponentsGroupIdentity",
            joinColumns = {@JoinColumn(name = "Identity_Id")},
            inverseJoinColumns = {@JoinColumn(name = "GroupId9C84CA727C3E880E")}
    )
    @JsonProperty("groups")
    Collection<Group> group = new HashSet<>();

    @Transient
    String cn;

    @SneakyThrows
    public static User fromString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(User.class, UserPlatformMixIn.class);
        return mapper.readValue(json, User.class);
    }

    @SneakyThrows
    public String toPlatformString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(this.getClass(), UserPlatformMixIn.class);
        return mapper.writeValueAsString(this);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCN() {
        return "cn=" + this.name + ",cn=organizational users,o=aca,cn=cordys,cn=defaultInst,o=appworks-aca.local";
    }
}
