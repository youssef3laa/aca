package com.asset.appwork.otds;

import com.asset.appwork.dto.Account;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class Otds {
    Account account;
    String apiBaseUrl;
    String partition;

    public Otds(Account account, String apiBaseUrl, String partition) {
        this.account = account;
        this.apiBaseUrl = apiBaseUrl;
        this.partition = partition;
    }

    public String getPartition() {
        return this.partition;
    }

    public static <T> String login(Environment env, T data) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setData(data.toString())
                .post(SystemUtil.generateOtdsAPIBaseUrl(env) + API.LOGIN.getUrl());
        return SystemUtil.readJSONField(http.getResponse(), "ticket");
    }

    public <T> String consolidate(T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + API.CONSOLIDATION_CONSOLIDATE.getUrl());

        return http.getResponse();
    }

    public <T> String createGroup(T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + API.GROUPS_CREATE.getUrl());

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String deleteGroupByGroupName(String roleName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .delete(this.apiBaseUrl + String.format(API.GROUPS_DELETE.getUrl(), URLEncoder.encode(roleName, StandardCharsets.UTF_8.name())));
        return http.getResponse();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public <T> String updateGroupByGroupName(String oldGroupName, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .put(this.apiBaseUrl + String.format(API.GROUPS_UPDATE.getUrl(), URLEncoder.encode(oldGroupName + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String getAllGroups() {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.GROUPS_GET_ALL.getUrl(), URLEncoder.encode(String.format("where_partition_name=%s", this.partition), StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String getGroupByGroupName(String roleName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.GROUPS_GET.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String getMembersAssignedToGroupByGroupName(String roleName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.GROUPS_GET_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public <T> String assignMembersToGroup(String roleName, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.GROUPS_ASSIGN_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public <T> String unAssignMembersFromGroup(String roleName, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.GROUPS_UNASSIGN_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public <T> String assignGroupsToGroup(String roleName, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.GROUPS_ASSIGN_GROUPS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public <T> String unAssignGroupsFromGroup(String roleName, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.GROUPS_UNASSIGN_GROUPS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String getUsersAssignedToGroupByGroupId(String roleName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.GROUPS_GET_USERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String resetPassword(String userId, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .put(this.apiBaseUrl + String.format(API.USERS_RESET_PASSWORD.getUrl(), userId));
        return http.getResponse();
    }

    public <T> String createUser(T data) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + API.USERS_CREATE.getUrl());

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String getAllUsers() {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + API.USERS_GET_ALL.getUrl() + "?" + URLEncoder.encode(String.format("where_partition_name=%s", this.partition), StandardCharsets.UTF_8.name()));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String getUserByUserName(String userName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.USERS_GET.getUrl(), URLEncoder.encode(userName + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public <T> String updateUserByUserId(String userId, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .put(this.apiBaseUrl + String.format(API.USERS_UPDATE.getUrl(), URLEncoder.encode(userId, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String deleteUserByUserId(String userId) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .delete(this.apiBaseUrl + String.format(API.USERS_DELETE.getUrl(), URLEncoder.encode(userId, StandardCharsets.UTF_8.name())));
        return http.getResponse();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public String getAllGroupsAssignedToUserByUserName(String username) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.USERS_GET_GROUPS.getUrl(), URLEncoder.encode(username + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public <T> String assignUserToGroupsByUserId(String userId, T data) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.USERS_ASSIGN_GROUP.getUrl(), URLEncoder.encode(userId, StandardCharsets.UTF_8.name())));
        return http.getResponse();
    }


    private enum API {
        LOGIN("/authentication/credentials"),
        CONSOLIDATION_CONSOLIDATE("/consolidation"),
        GROUPS_ASSIGN_MEMBERS("/groups/%s/members"),
        GROUPS_ASSIGN_GROUPS("/groups/%s/groups"),
        GROUPS_CREATE("/groups"),
        GROUPS_DELETE("/groups/%s"),
        GROUPS_GET("/groups/%s"),
        GROUPS_GET_ALL("/groups"),
        GROUPS_GET_MEMBERS("/groups/%s/members"),
        GROUPS_GET_USERS("/groups/%s/users"),
        GROUPS_UNASSIGN_MEMBERS("/groups/%s/members/deletionset"),
        GROUPS_UNASSIGN_GROUPS("/groups/%s/groups/deletionset"),
        GROUPS_UPDATE("/groups/%s"),
        USERS_ASSIGN_GROUP("/users/%s/memberof"),
        USERS_CREATE("/users"),
        USERS_DELETE("/users/%s"),
        USERS_GET("/users/%s"),
        USERS_GET_ALL("/users"),
        USERS_GET_GROUPS("/users/%s/memberof"),
        USERS_RESET_PASSWORD("/users/%s/password"),
        USERS_UPDATE("/users/%s");

        private final String url;
        API(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    }
}
