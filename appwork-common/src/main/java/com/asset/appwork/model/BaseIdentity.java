package com.asset.appwork.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Optional;

@MappedSuperclass
public abstract class BaseIdentity<T extends BaseIdentity> {
    @Id
    @Column(name = "Id")
    long id;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @Column(name = "Name_en")
    String name_en;
    @Column(name = "Name_ar")
    String name_ar;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Optional.ofNullable(description).orElse("");
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
}
