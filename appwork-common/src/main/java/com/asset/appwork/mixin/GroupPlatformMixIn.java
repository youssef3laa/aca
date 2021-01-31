package com.asset.appwork.mixin;

import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class GroupPlatformMixIn {
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

    @JsonAlias({"groupCode", "GroupCode"})
    @JsonProperty("GroupCode")
    String groupCode;
    @JsonAlias({"isHeadRole", "Head"})
    @JsonProperty("Head")
    Boolean isHeadRole;
    @JsonAlias({"isViceRole", "Vice"})
    @JsonProperty("Vice")
    Boolean isViceRole;
    @JsonIgnore
    Unit unit;
    @JsonIgnore
    String cn;
}
