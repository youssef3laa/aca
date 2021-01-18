package com.asset.appwork.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Optional;

@Entity(name = "Person")
@Table(name = "O9OpenTextEntityIdentityComponentsPerson")
public class Person {
    @Id
    @Column(name = "Id")
    long id;
    @Column(name = "User_ID")
    String userId;
    @Column(name = "title")
    String title;
    @Column(name = "FirstName")
    String firstName;
    @Column(name = "MiddleName")
    String middleName;
    @Column(name = "LastName")
    String lastName;
    @Column(name = "Email")
    String email;
    @Column(name = "Phone")
    String phone;
    @Column(name = "Mobile")
    String mobile;
    @Column(name = "notes")
    String notes;

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return Optional.ofNullable(title).orElse("");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return Optional.ofNullable(firstName).orElse("");
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return Optional.ofNullable(middleName).orElse("");
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return Optional.ofNullable(lastName).orElse("");
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return Optional.ofNullable(email).orElse("");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return Optional.ofNullable(phone).orElse("");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return Optional.ofNullable(mobile).orElse("");
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNotes() {
        return Optional.ofNullable(notes).orElse("");
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
