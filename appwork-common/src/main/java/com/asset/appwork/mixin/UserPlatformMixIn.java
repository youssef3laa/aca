package com.asset.appwork.mixin;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Person;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
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

    @JsonProperty("UserId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("IdentityDisplayName")
    public String getDisplayName() {
        return displayName;
    }
}
