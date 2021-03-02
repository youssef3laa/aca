package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;

@Entity(name = "Escalation")
@Table(name = "AssetGeneralACAACA_Entity_Escalation")
public class Escalation {
    @Id
    @Column(name = "Id")
    long id;
    @Column(name = "duration")
    Integer duration;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "JobType_Id",
            referencedColumnName = "Id"
    )
    @JsonProperty("jobType")
    Lookup jobType;

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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Lookup getJobType() {
        return jobType;
    }

    public void setJobType(Lookup jobType) {
        this.jobType = jobType;
    }
}
