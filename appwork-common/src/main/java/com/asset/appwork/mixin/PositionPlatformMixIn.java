package com.asset.appwork.mixin;

import com.asset.appwork.model.Assignment;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public abstract class PositionPlatformMixIn {
    @JsonIgnore
    Long id;
    @JsonProperty("PositionName")
    String name;
    @JsonProperty("Description")
    String description;
    @JsonProperty("Lead")
    Boolean isLead;
    @JsonIgnore
    Unit unit;
    @JsonIgnore
    Collection<Assignment> assignment;
}
