package com.asset.appwork.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {
    String userPartitionID;
    String name;
    String description;
    List<Values> values;

    public Member() {
    }

    public Member(String userPartitionId, String name, List<Values> values) {
        this.userPartitionID = userPartitionId;
        this.name = name;
        this.values = values;
    }

    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public String getPasswordResetJsonString(String newPassword) {
        return new PasswordReset(newPassword).toString();
    }

    public String getUserPartitionID() {
        return userPartitionID;
    }

    public void setUserPartitionID(String userPartitionID) {
        this.userPartitionID = userPartitionID;
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

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }


    public static class Values {
        String name;
        List<String> values;

        public Values(String name, List<String> values) {
            this.name = name;
            this.values = values;
        }

        @SneakyThrows
        public String toString() {
            return new ObjectMapper().writeValueAsString(this);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }

    private static class PasswordReset {
        String newPassword;

        public PasswordReset(String newPassword) {
            this.newPassword = newPassword;
        }

        @SneakyThrows
        public String toString() {
            return new ObjectMapper().writeValueAsString(this);
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    public static class TargetId {
        String targetId;

        public TargetId(Long targetId) {
            this.targetId = targetId.toString();
        }

        @SneakyThrows
        public String toString() {
            return new ObjectMapper().writeValueAsString(this);
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }
    }

    public static class StringList {
        List<String> stringList;

        public StringList(List<String> stringList) {
            this.stringList = stringList;
        }

        @SneakyThrows
        public String toString() {
            return new ObjectMapper().writeValueAsString(this);
        }

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }
    }
}