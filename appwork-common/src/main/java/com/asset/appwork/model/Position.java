package com.asset.appwork.model;

import com.asset.appwork.mixin.PositionPlatformMixIn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;

@Entity(name = "Position")
@Table(name = "OpenTextEntityIdentityComponentsPosition")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Position {
    @Id
    @Column(name = "Id1")
    Long id;
    @Column(name = "PositionName")
    String name;
    @Column(name = "Description")
    String description;
    @Column(name = "Lead")
    Boolean isLead;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "Id",
            referencedColumnName = "Id"
    )
//    @JsonProperty("unit")
    @JsonIgnore
    Unit unit;

    @OneToMany(mappedBy = "position", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonProperty("assignments")
//    @JsonIgnore
    Collection<Assignment> assignment;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    @SneakyThrows
    public static Position fromString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Position.class, PositionPlatformMixIn.class);
        return mapper.readValue(json, Position.class);
    }

    @SneakyThrows
    public String toPlatformString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(this.getClass(), PositionPlatformMixIn.class);
        return mapper.writeValueAsString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Optional.ofNullable(description).orElse("");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsLead() {
        return isLead;
    }

    public void setIsLead(Boolean isLead) {
        this.isLead = isLead;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
