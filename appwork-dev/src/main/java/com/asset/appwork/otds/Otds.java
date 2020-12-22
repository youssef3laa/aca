package com.asset.appwork.otds;

import com.asset.appwork.dto.Account;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public <T> String createRole(T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + API.ROLES_CREATE.getUrl());

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String deleteRoleByRoleName(String roleName) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .delete(this.apiBaseUrl + String.format(API.ROLES_DELETE.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return http.getResponse();
    }

    public <T> String updateRoleByRoleName(String oldRoleName, T data) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .put(this.apiBaseUrl + String.format(API.ROLES_UPDATE.getUrl(), URLEncoder.encode(oldRoleName + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getAllRoles() throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.ROLES_GET_ALL.getUrl(), URLEncoder.encode(String.format("where_partition_name=%s", this.partition), StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getRoleByRoleName(String roleName) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.ROLES_GET.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getMembersAssignedToRoleByRoleName(String roleName) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.ROLES_GET_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String assignMembersToRole(String roleName, T data) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.ROLES_ASSIGN_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    public <T> String unAssignMembersFromRole(String roleName, T data) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.ROLES_UNASSIGN_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    public <T> String assignRolesToRole(String roleName, T data) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.ROLES_ASSIGN_ROLES.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    public <T> String unAssignRolesFromRole(String roleName, T data) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.ROLES_UNASSIGN_ROLES.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return http.getResponse();
    }

    public String getUsersAssignedToRoleByRoleId(String roleName) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.ROLES_GET_USERS.getUrl(), URLEncoder.encode(roleName + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String resetPassword(String username, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .put(this.apiBaseUrl + String.format(API.USERS_RESET_PASSWORD.getUrl(), username));
        return http.getResponse();
    }

    public <T> String createUser(T data) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + API.USERS_CREATE.getUrl());

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getAllUsers() throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + API.USERS_GET_ALL.getUrl() + "?" + URLEncoder.encode(String.format("where_partition_name=%s", this.partition), StandardCharsets.UTF_8.name()));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getUserByUserName(String userName) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.USERS_GET.getUrl(), URLEncoder.encode(userName + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String updateUserByUserName(String oldUsername, T data) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .put(this.apiBaseUrl + String.format(API.USERS_UPDATE.getUrl(), URLEncoder.encode(oldUsername + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String deleteUserByUserName(String username) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .delete(this.apiBaseUrl + String.format(API.USERS_DELETE.getUrl(), URLEncoder.encode(username + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return http.getResponse();
    }

    public String getAllRolesAssignedToUserByUserName(String username) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .get(this.apiBaseUrl + String.format(API.USERS_GET_ROLES.getUrl(), URLEncoder.encode(username + "@" + this.partition, StandardCharsets.UTF_8.name())));

        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String assignUserToRoleByUserId(String username, T data) throws UnsupportedEncodingException, JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", this.account.getTicket())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.USERS_ASSIGN_ROLE.getUrl(), URLEncoder.encode(username + "@" + this.partition, StandardCharsets.UTF_8.name())));
        return http.getResponse();
    }


    private enum API {
        LOGIN("/authentication/credentials"),
        CONSOLIDATION_CONSOLIDATE("/consolidation"),
        ROLES_ASSIGN_MEMBERS("/roles/%s/members"),
        ROLES_ASSIGN_ROLES("/roles/%s/roles"),
        ROLES_CREATE("/roles"),
        ROLES_DELETE("/roles/%s"),
        ROLES_GET("/roles/%s"),
        ROLES_GET_ALL("/roles"),
        ROLES_GET_MEMBERS("/roles/%s/members"),
        ROLES_GET_USERS("/roles/%s/users"),
        ROLES_UNASSIGN_MEMBERS("/roles/%s/members/deletionset"),
        ROLES_UNASSIGN_ROLES("/roles/%s/roles/deletionset"),
        ROLES_UPDATE("/roles/%s"),
        USERS_ASSIGN_ROLE("/users/%s/roles"),
        USERS_CREATE("/users"),
        USERS_DELETE("/users/%s"),
        USERS_GET("/users/%s"),
        USERS_GET_ALL("/users"),
        USERS_GET_ROLES("/users/%s/roles"),
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
