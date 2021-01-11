package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity(name = "Position")
@Table(name = "O9OpenTextEntityIdentityComponentsPosition")
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getLead() {
        return isLead;
    }

    public void setLead(Boolean lead) {
        isLead = lead;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
