package com.asset.appwork.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.Table;

@Entity(name = "O9OpenTextEntityIdentityComponentsIdentity")
//@Table(name = "O9OpenTextEntityIdentityComponentsIdentity")
public class Group {
    @Id
    @Column(name="Id")
    long id;
    @Column(name="Name")
    String Name;
    @Column(name="Description")
    String description;
    @Column(name="Name_en")
    String name_en;
    @Column(name="Name_ar")
    String name_ar;
    @Column(name="GroupCode")
    String groupCode;

    @SneakyThrows
    public String toString(){
        return new ObjectMapper().writeValueAsString(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
