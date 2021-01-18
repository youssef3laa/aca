package com.asset.appwork.mixin;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashSet;

public abstract class UserPlatformMixIn {
    @JsonIgnore
    Long id;
    String name;
    String description;
    String name_en;
    String name_ar;

    String userId;
    String displayName;
    @JsonIgnore
    Person person;
    @JsonIgnore
    Collection<Group> group = new HashSet<>();
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

    @JsonProperty("UserId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("IdentityDisplayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
