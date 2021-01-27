package com.asset.appwork.mixin;

import com.asset.appwork.model.Person;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.annotation.*;

import java.util.Date;

public abstract class AssignmentPlatformMixIn {
    @JsonIgnore
    Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonAlias({"startDate", "Start_Date"})
    @JsonProperty("Start_Date")
    Date startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonAlias({"endDate", "End_Date"})
    @JsonProperty("End_Date")
    Date endDate;
    @JsonAlias({"principal", "Principal"})
    @JsonProperty("Principal")
    Boolean principal;
    @JsonIgnore
    Unit unit;
    @JsonIgnore
    Position position;
    @JsonIgnore
    Person person;
}
