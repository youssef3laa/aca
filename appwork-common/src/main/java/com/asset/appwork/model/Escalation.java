package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Escalation")
@Table(name = "AssetGeneralACAACA_Entity_Escalation")
public class Escalation {
    @Id
    @Column(name = "Id")
    long id;
    @Column(name = "jobType")
    Integer jobType;
    @Column(name = "duration")
    Integer duration;
    @Column(name = "extension")
    Integer extension;
    @Column(name = "unitType")
    Integer unitType;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    @SneakyThrows
    public static Escalation fromString(String json) {
        return new ObjectMapper().readValue(json, Escalation.class);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getExtension() {
        return extension;
    }

    public void setExtension(Integer extension) {
        this.extension = extension;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }
}
