package com.asset.appwork.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class UserMember {
    String username;
    String password;
    String email;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public Member getMember(String partition) {
        List<Member.Values> memberValues = new ArrayList<>();
        List<String> emailValues = new ArrayList<>();
        emailValues.add(this.email);
        memberValues.add(new Member.Values("mail", emailValues));
        return new Member(partition, username, memberValues);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
