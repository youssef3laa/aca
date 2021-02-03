package com.asset.appwork.mixin;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public abstract class UnitPlatformMixIn {
    @JsonIgnore
    Long id;
    @JsonAlias({"name", "Name"})
    @JsonProperty("Name")
    String name;
    @JsonAlias({"description", "Description"})
    @JsonProperty("Description")
    String description;
    @JsonAlias({"name_en", "Name_en"})
    @JsonProperty("Name_en")
    String name_en;
    @JsonAlias({"name_ar", "Name_ar"})
    @JsonProperty("Name_ar")
    String name_ar;
    @JsonAlias({"displayName", "IdentityDisplayName"})
    @JsonProperty("IdentityDisplayName")
    String displayName;

    @JsonAlias({"unitTypeCode", "UnitTypeCode"})
    @JsonProperty("UnitTypeCode")
    String unitTypeCode;
    @JsonAlias({"unitCode", "UnitCode"})
    @JsonProperty("UnitCode")
    String unitCode;
    @JsonIgnore
    Collection<Group> group;
    @JsonIgnore
    Collection<Unit> child;
    @JsonIgnore
    Collection<Position> position;
    @JsonIgnore
    Collection<Unit> parent;
}
