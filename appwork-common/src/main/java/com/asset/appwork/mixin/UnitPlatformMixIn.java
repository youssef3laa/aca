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

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Name_en")
    public String getName_en() {
        return name_en;
    }

    @JsonProperty("name_en")
    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    @JsonProperty("Name_ar")
    public String getName_ar() {
        return name_ar;
    }

    @JsonProperty("name_ar")
    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    @JsonProperty("UnitTypeCode")
    public String getUnitTypeCode() {
        return unitTypeCode;
    }

    @JsonProperty("unitTypeCode")
    public void setUnitTypeCode(String unitTypeCode) {
        this.unitTypeCode = unitTypeCode;
    }

    @JsonProperty("UnitCode")
    public String getUnitCode() {
        return unitCode;
    }

    @JsonProperty("unitCode")
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
