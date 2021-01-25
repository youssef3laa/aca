package com.asset.appwork.mixin;

import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class GroupPlatformMixIn {
    @JsonIgnore
    Long id;
    String name;
    String description;
    String name_en;
    String name_ar;

    String groupCode;
    Boolean isHeadRole;
    Boolean isViceRole;
    @JsonIgnore
    Unit unit;
    @JsonIgnore
    String cn;

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

    @JsonProperty("GroupCode")
    public String getGroupCode() {
        return groupCode;
    }

    @JsonProperty("Head")
    public Boolean getIsHeadRole() {
        return isHeadRole;
    }

    @JsonProperty("Vice")
    public Boolean getIsViceRole() {
        return isViceRole;
    }
}
