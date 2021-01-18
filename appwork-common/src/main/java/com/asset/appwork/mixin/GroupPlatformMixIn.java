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

    @JsonProperty("GroupCode")
    public String getGroupCode() {
        return groupCode;
    }

    @JsonProperty("groupCode")
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @JsonProperty("Head")
    public Boolean getHeadRole() {
        return isHeadRole;
    }

    @JsonProperty("isHeadRole")
    public void setHeadRole(Boolean headRole) {
        isHeadRole = headRole;
    }

    @JsonProperty("Vice")
    public Boolean getViceRole() {
        return isViceRole;
    }

    @JsonProperty("isViceRole")
    public void setViceRole(Boolean viceRole) {
        isViceRole = viceRole;
    }
}
