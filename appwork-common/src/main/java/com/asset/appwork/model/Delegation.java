package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "Delegation")
@Table(name = "AssetGeneralACAACA_Entity_Delegation")
public class Delegation {
    @Id
    @Column(name = "Id")
    long id;
    @Column(name = "fromDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date from;
    @Column(name = "toDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date to;
    @Column(name = "role")
    String role;
    @Column(name = "userId")
    String userId;
    @Column(name = "delegatedTo")
    String delegatedTo;
    @Column(name = "isActive")
    Boolean isActive;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    @SneakyThrows
    public static Delegation fromString(String json) {
        return new ObjectMapper().readValue(json, Delegation.class);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDelegatedTo() {
        return delegatedTo;
    }

    public void setDelegatedTo(String delegatedTo) {
        this.delegatedTo = delegatedTo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
