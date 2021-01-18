package com.asset.appwork.mixin;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public abstract class UnitPlatformMixIn {
    @JsonIgnore
    Long id;
    String name;
    String description;
    String name_en;
    String name_ar;

    String unitTypeCode;
    String unitCode;
    @JsonIgnore
    Collection<Group> group;
    @JsonIgnore
    Collection<Unit> child;
    @JsonIgnore
    Collection<Position> position;
    @JsonIgnore
    Collection<Unit> parent;

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Name_en")
    public String getName_en() {
        return name_en;
    }

    @JsonProperty("Name_ar")
    public String getName_ar() {
        return name_ar;
    }

    @JsonProperty("UnitTypeCode")
    public String getUnitTypeCode() {
        return unitTypeCode;
    }

    @JsonProperty("UnitCode")
    public String getUnitCode() {
        return unitCode;
    }
}
