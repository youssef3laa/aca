package com.asset.appwork.dto;

import com.asset.appwork.model.Lookup;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class EscalationDTO {
    Long id;
    Integer duration;
    Integer extension;
    Lookup jobType;
    Lookup unitType;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Lookup getJobType() {
        return jobType;
    }

    public void setJobType(Lookup jobType) {
        this.jobType = jobType;
    }

    public Lookup getUnitType() {
        return unitType;
    }

    public void setUnitType(Lookup unitType) {
        this.unitType = unitType;
    }
}
