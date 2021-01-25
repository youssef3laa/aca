package com.asset.appwork.mixin;

import com.asset.appwork.model.Assignment;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public abstract class PositionPlatformMixIn {
    @JsonIgnore
    Long id;
    String name;
    String description;
    Boolean isLead;
    @JsonIgnore
    Unit unit;
    @JsonIgnore
    Collection<Assignment> assignment;

    @JsonProperty("PositionName")
    public String getName() {
        return name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Lead")
    public Boolean getIsLead() {
        return isLead;
    }
}
