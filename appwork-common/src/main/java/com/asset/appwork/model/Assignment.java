package com.asset.appwork.model;

import com.asset.appwork.mixin.AssignmentPlatformMixIn;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Assignment")
@Table(name = "O9OpenTextEntityIdentityComponentsAssignment")
public class Assignment {
    @Id
    @Column(name = "Id")
    Long id;
    //    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Start_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date startDate;
    //    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "End_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date endDate;
    @Column(name = "Principal")
    Boolean principal;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "toPositionToOne_Id",
            referencedColumnName = "Id"
    )
//    @JsonProperty("unit")
    @JsonIgnore
    Unit unit;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "toPositionToOne_Id1",
            referencedColumnName = "Id1"
    )
//    @JsonProperty("position")
    @JsonIgnore
    Position position;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "toPersonToOne_Id",
            referencedColumnName = "Id"
    )
//    @JsonProperty("person")
    @JsonIgnore
    Person person;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    @SneakyThrows
    public String toPlatformString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(this.getClass(), AssignmentPlatformMixIn.class);
        return mapper.writeValueAsString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
