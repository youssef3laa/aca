package com.asset.appwork.mixin;

import com.asset.appwork.model.Person;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public abstract class AssignmentPlatformMixIn {
    @JsonIgnore
    Long id;
    @JsonProperty("Start_Date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date startDate;
    @JsonProperty("End_Date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date endDate;
    @JsonProperty("Principal")
    Boolean principal;
    @JsonIgnore
    Unit unit;
    @JsonIgnore
    Position position;
    @JsonIgnore
    Person person;
}
