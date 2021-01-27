package com.asset.appwork.mixin;

import com.asset.appwork.model.Assignment;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public abstract class PositionPlatformMixIn {
    @JsonIgnore
    Long id;
    @JsonAlias({"name", "PositionName"})
    @JsonProperty("PositionName")
    String name;
    @JsonAlias({"description", "Description"})
    @JsonProperty("Description")
    String description;
    @JsonAlias({"isLead", "Lead"})
    @JsonProperty("Lead")
    Boolean isLead;
    @JsonIgnore
    Unit unit;
    @JsonIgnore
    Collection<Assignment> assignment;
}
