package com.asset.appwork.dto;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class DelegationDTO {
    Long id;
    String from;
    String to;
    Group group;
    User user;
    Group delegatedGroup;
    Boolean isActive;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getDelegatedGroup() {
        return delegatedGroup;
    }

    public void setDelegatedGroup(Group delegatedGroup) {
        this.delegatedGroup = delegatedGroup;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
