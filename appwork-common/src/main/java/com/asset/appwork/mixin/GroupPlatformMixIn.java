package com.asset.appwork.mixin;

import com.asset.appwork.enums.GroupType;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public abstract class GroupPlatformMixIn {
    @JsonIgnore
    Long id;
    @JsonAlias({"name", "Name"})
    @JsonProperty("Name")
    String name;
    @JsonAlias({"description", "Description"})
    @JsonProperty("Description")
    String description;
    @JsonAlias({"nameEn", "Name_en"})
    @JsonProperty("Name_en")
    String nameEn;
    @JsonAlias({"nameAr", "Name_ar"})
    @JsonProperty("Name_ar")
    String nameAr;
    @JsonAlias({"displayName", "IdentityDisplayName"})
    @JsonProperty("IdentityDisplayName")
    String displayName;

    @JsonAlias({"groupCode", "GroupCode"})
    @JsonProperty("GroupCode")
    String groupCode;
    @JsonAlias({"type", "Type"})
    @Enumerated(EnumType.STRING)
    @JsonProperty("Type")
    GroupType type;
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
