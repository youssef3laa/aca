package com.asset.appwork.mixin;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Person;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

public abstract class UserPlatformMixIn {
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

    @JsonAlias({"userId", "UserId"})
    @JsonProperty("UserId")
    String userId;
    @JsonAlias({"displayName", "IdentityDisplayName"})
    @JsonProperty("IdentityDisplayName")
    String displayName;
    @JsonIgnore
    Person person;
    @JsonIgnore
    Collection<Group> group = new HashSet<>();
    @JsonIgnore
    String cn;
}
