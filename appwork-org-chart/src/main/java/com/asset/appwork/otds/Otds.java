package com.asset.appwork.otds;

import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class Otds {
    public static final String HOST = "appworks-aca";
    public static final String PORT = "8080";
    public static final String API_BASE_URL = "http://" + HOST + ":" + PORT + "/otdsws/rest";
    public static final String PARTITION = "aw.aca";
    public static final String DEFAULT_EMAIL_PROVIDER = "@asset.aca.gov.eg";
    public static final String DEFAULT_USER_PASSWORD = "Asset99a";
    public static final String USER_ROLE_FORMAT = "cn=%s,ou=Root,ou=" + PARTITION + ",ou=IdentityProviders,dc=identity,dc=opentext,dc=net";
    public static final String UNIT_FORMAT = "ou=%s,ou=Root,ou=" + PARTITION + ",ou=IdentityProviders,dc=identity,dc=opentext,dc=net";

    private String ticket = "";

    private enum API {
        LOGIN("/authentication/credentials"),
        CONSOLIDATION_CONSOLIDATE("/consolidation"),
        ROLES_ASSIGN_CUSTOM_ATTRIBUTE("/roles/%s/attribute"),
        ROLES_ASSIGN_MEMBERS("/roles/%s/members"),
        ROLES_ASSIGN_ROLES("/roles/%s/roles"),
        ROLES_CREATE("/roles"),
        ROLES_DELETE("/roles/%s"),
        ROLES_GET("/roles/%s"),
        ROLES_GET_ALL("/roles"),
        ROLES_GET_MEMBERS("/roles/%s/members"),
        ROLES_GET_USERS("/roles/%s/users"),
        ROLES_UNASSIGN_CUSTOM_ATTRIBUTE("/roles/%s/attribute/deletionset"),
        ROLES_UNASSIGN_MEMBERS("/roles/%s/members/deletionset"),
        ROLES_UNASSIGN_ROLES("/roles/%s/roles/deletionset"),
        ROLES_UPDATE("/roles/%s"),
        ORGUNITS_CREATE("/orgunits"),
        ORGUNITS_DELETE("/orgunits/%s"),
        ORGUNITS_GET("/orgunits/%s"),
        ORGUNITS_GET_CHILDREN("/orgunits/%s/children"),
        ORGUNITS_GET_PARENTS("/orgunits/%s/parents"),
        ORGUNITS_GET_ROLES("/orgunits/%s/roles"),
        ORGUNITS_GET_ROOT("/orgunits"),
        ORGUNITS_GET_USERS("/orgunits/%s/users"),
        ORGUNITS_UPDATE("/orgunits/%s"),
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
            this.url = API_BASE_URL + url;
        }

        public String getUrl() {
            return this.url;
        }
    }

    public Otds(String username, String password) throws JsonProcessingException {
       this(login(username, password));
    }

    public Otds(String ticket) throws JsonProcessingException {
        this.ticket = ticket;
    }

    public static String login(String userName, String password) throws JsonProcessingException {
        String url = API.LOGIN.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setData("{" +
                        "    \"userName\": \"" + userName + "\"," +
                        "    \"password\": \"" + password + "\"" +
                        "}")
                .post(url);
        return SystemUtil.readJSONField(http.getResponse(), "ticket");
    }

    public static String login(String userName, String password, String resourceId) throws JsonProcessingException {
        String url = API.LOGIN.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setData("{" +
                        "    \"userName\": \"" + userName + "\"," +
                        "    \"password\": \"" + password + "\"," +
                        "    \"targetResourceId\": \"" + resourceId + "\"" +
                        "}")
                .post(url);
        return SystemUtil.readJSONField(http.getResponse(), "ticket");
    }

    public String getPlatformAuthHeader() {
        return  "  <SOAP:Header>" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">" +
                "      <AuthenticationToken>" + ticket + "</AuthenticationToken>" +
                "    </OTAuthentication>" +
                "  </SOAP:Header>";
    }

    public String consolidate(String objectToConsolidateDN) {
        String url = API.CONSOLIDATION_CONSOLIDATE.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"objectToConsolidate\": \"" + objectToConsolidateDN + "\"" +
                        "}")
                .post(url);
        return http.getResponse();
    }

    public String assignCustomAttributeToRole(String roleName, ArrayList<CustomAttribute> attrs) throws UnsupportedEncodingException, JsonProcessingException {
        String url = String.format(API.ROLES_ASSIGN_CUSTOM_ATTRIBUTE.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));

        String attrsStr = new ObjectMapper().writeValueAsString(attrs);

        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"customAttributeList\": " +
                        attrsStr +
                        "}")
                .post(url);

        return http.getResponse();
    }

    public String unAssignCustomAttributeFromRole(String roleName, ArrayList<CustomAttribute> attrs) throws UnsupportedEncodingException, JsonProcessingException {
        String url = String.format(API.ROLES_UNASSIGN_CUSTOM_ATTRIBUTE.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));

        String attrsStr = new ObjectMapper().writeValueAsString(attrs);

        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"customAttributeList\": " +
                        attrsStr +
                        "}")
                .post(url);

        return http.getResponse();
    }

    // TODO: Add more details to be added
    public String createRole(String roleName) {
        String url = API.ROLES_CREATE.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"userPartitionID\": \"" + PARTITION + "\"," +
                        "    \"name\": \"" + roleName + "\"" +
                        "}")
                .post(url);

        return http.getResponse();
    }


    public String deleteRoleByRoleId(String roleName) throws UnsupportedEncodingException {
        String url = String.format(API.ROLES_DELETE.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .delete(url);
        return http.getResponse();
    }

    // TODO: Add more details to be added
    public String updateRoleByRoleId(String oldRoleName, String newRoleName) throws UnsupportedEncodingException {
        String url = String.format(API.ROLES_UPDATE.getUrl(), URLEncoder.encode(oldRoleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"userPartitionID\": \"" + PARTITION + "\"," +
                        "    \"name\": \"" + newRoleName + "\"" +
                        "}")
                .put(url);
        return http.getResponse();
    }

    public String getAllRoles() throws UnsupportedEncodingException {
        String url = String.format(API.ROLES_GET_ALL.getUrl(), URLEncoder.encode(String.format("where_partition_name=%s", PARTITION), StandardCharsets.UTF_8.name()));
        System.out.println(url);
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);

//        return SystemUtil.readJSONField(http.getResponse(), "users");
        return http.getResponse();
    }

    public String getRoleByRoleId(String roleName) throws UnsupportedEncodingException {
        String url = String.format(API.ROLES_GET.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);
        return http.getResponse();
    }

    public String getMembersAssignedToRoleByRoleId(String roleName) throws UnsupportedEncodingException {
        String url = String.format(API.ROLES_GET_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);

//        return SystemUtil.readJSONField(http.getResponse(), "users");
        return http.getResponse();
    }

    public String assignMembersToRole(String roleName, ArrayList<String> memberDNs) throws UnsupportedEncodingException, JsonProcessingException {
        String url = String.format(API.ROLES_ASSIGN_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));

        String membersStr = new ObjectMapper().writeValueAsString(memberDNs);

        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"stringList\": " +
                        membersStr +
                        "}")
                .post(url);

        return http.getResponse();
    }

    public String unAssignMembersFromRole(String roleName, ArrayList<String> memberDNs) throws UnsupportedEncodingException, JsonProcessingException {
        String url = String.format(API.ROLES_UNASSIGN_MEMBERS.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));

        String membersStr = new ObjectMapper().writeValueAsString(memberDNs);

        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"stringList\": " +
                        membersStr +
                        "}")
                .post(url);

        return http.getResponse();
    }

    public String assignRolesToRole(String roleName, ArrayList<String> rolesDNs) throws UnsupportedEncodingException, JsonProcessingException {
        String url = String.format(API.ROLES_ASSIGN_ROLES.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));

        String rolesStr = new ObjectMapper().writeValueAsString(rolesDNs);

        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"stringList\": " +
                        rolesStr +
                        "}")
                .post(url);

        return http.getResponse();
    }

    public String unAssignRolesFromRole(String roleName, ArrayList<String> rolesDNs) throws UnsupportedEncodingException, JsonProcessingException {
        String url = String.format(API.ROLES_UNASSIGN_ROLES.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));

        String rolesStr = new ObjectMapper().writeValueAsString(rolesDNs);

        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "    \"stringList\": " +
                        rolesStr +
                        "}")
                .post(url);

        return http.getResponse();
    }

    public String getUsersAssignedToRoleByRoleId(String roleName) throws UnsupportedEncodingException {
        String url = String.format(API.ROLES_GET_USERS.getUrl(), URLEncoder.encode(roleName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);

//        return SystemUtil.readJSONField(http.getResponse(), "users");
        return http.getResponse();
    }

    public String getRootOrgUnit() throws UnsupportedEncodingException {
        String url = API.ORGUNITS_GET_ROOT.getUrl() + "?" + URLEncoder.encode(String.format("where_partition_name=%s", PARTITION), StandardCharsets.UTF_8.name());
        System.out.println(url);
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);

//        return SystemUtil.readJSONField(http.getResponse(), "users");
        return http.getResponse();
    }

    // TODO: Add more details to be added
    public String createOrgUnit(String unitName) {
        String url = API.ORGUNITS_CREATE.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "\"userPartitionID\": \"" + PARTITION + "\"," +
                        "\"name\": \"" + unitName + "\"" +
                        "}")
                .post(url);

        return http.getResponse();
    }

    public String getOrgUnitByName(String unitName) throws UnsupportedEncodingException {
        String url = String.format(API.ORGUNITS_GET.getUrl(), URLEncoder.encode(String.format(UNIT_FORMAT, unitName), StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);
        return http.getResponse();
    }

    // Check response error (works with an error)
    // TODO: Add more details to be added
    public String updateOrgUnit(String oldUnitName, String newUnitName) throws UnsupportedEncodingException {
        String url = String.format(API.ORGUNITS_UPDATE.getUrl(), URLEncoder.encode(String.format(UNIT_FORMAT, oldUnitName), StandardCharsets.UTF_8.name()));
        System.out.println(url);
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "\"userPartitionID\": \"" + PARTITION + "\"," +
                        "\"name\": \"" + newUnitName + "\"" +
                        "}")
                .put(url);
        return http.getResponse();
    }

    public String deleteOrgUnitByName(String unitName) throws UnsupportedEncodingException {
        String url = String.format(API.ORGUNITS_DELETE.getUrl(), URLEncoder.encode(String.format(UNIT_FORMAT, unitName), StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .delete(url);
        return http.getResponse();
    }

    public String getOrgUnitChildrenByName(String unitName) throws UnsupportedEncodingException {
        String url = String.format(API.ORGUNITS_GET_CHILDREN.getUrl(), URLEncoder.encode(String.format(UNIT_FORMAT, unitName), StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);
        return http.getResponse();
    }

    public String getOrgUnitParentsByName(String unitName) throws UnsupportedEncodingException {
        String url = String.format(API.ORGUNITS_GET_PARENTS.getUrl(), URLEncoder.encode(String.format(UNIT_FORMAT, unitName), StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);
        return http.getResponse();
    }

    public String getOrgUnitRolesByName(String unitName) throws UnsupportedEncodingException {
        String url = String.format(API.ORGUNITS_GET_ROLES.getUrl(), URLEncoder.encode(String.format(UNIT_FORMAT, unitName), StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);
        return http.getResponse();
    }

    public String getOrgUnitUsersByName(String unitName) throws UnsupportedEncodingException {
        String url = String.format(API.ORGUNITS_GET_USERS.getUrl(), URLEncoder.encode(String.format(UNIT_FORMAT, unitName), StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);
        return http.getResponse();
    }

    public String resetPassword(String userId, String password) {
        String url = String.format(API.USERS_RESET_PASSWORD.getUrl(), userId);
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "\"password\": \"" + password + "\"" +
                        "}")
                .put(url);
        return http.getResponse();
    }

    // TODO: Add more details to be added
    public String createUser(String username, String password) throws JsonProcessingException {
        String url = API.USERS_CREATE.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "\"userPartitionID\": \"" + PARTITION + "\"," +
                        "\"name\": \"" + username + "\"," +
                        "\"values\": [" +
                        "{" +
                        "\"name\": \"" + "mail" + "\"," +
                        "\"values\": [" +
                        "\"" + username + DEFAULT_EMAIL_PROVIDER + "\"" +
                        "]" +
                        "}" +
                        "]" +
                        "}")
                .post(url);

        String response = http.getResponse();
        this.resetPassword(SystemUtil.readJSONField(response, "id"), password);
//        this.consolidate(SystemUtil.readJSONField(response, "location"));
        return response;
    }
    public String createUser(String username) throws JsonProcessingException {
        return this.createUser(username, DEFAULT_USER_PASSWORD);
    }

    public String getAllUsers() throws UnsupportedEncodingException {
        String url = API.USERS_GET_ALL.getUrl() + "?" + URLEncoder.encode(String.format("where_partition_name=%s", PARTITION), StandardCharsets.UTF_8.name());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);

//        return SystemUtil.readJSONField(http.getResponse(), "users");
        return http.getResponse();
    }

    public String getUserByUserId(String userName) throws UnsupportedEncodingException {
        String url = String.format(API.USERS_GET.getUrl(), URLEncoder.encode(userName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);
        return http.getResponse();
    }

    // TODO: Add more details to be added
    public String updateUserByUserId(String oldUserId, String newUserId) throws UnsupportedEncodingException {
        String url = String.format(API.USERS_UPDATE.getUrl(), URLEncoder.encode(oldUserId + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "\"userPartitionID\": \"" + PARTITION + "\"," +
                        "\"name\": \"" + newUserId + "\"," +
                        "\"values\": [" +
                        "{" +
                        "\"name\": \"" + "mail" + "\"," +
                        "\"values\": [" +
                        "\"" + newUserId + DEFAULT_EMAIL_PROVIDER + "\"" +
                        "]" +
                        "}" +
                        "]" +
                        "}")
                .put(url);
        return http.getResponse();
    }

    public String deleteUserByUserId(String userName) throws UnsupportedEncodingException {
        String url = String.format(API.USERS_DELETE.getUrl(), URLEncoder.encode(userName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .delete(url);
        return http.getResponse();
    }

    public String getAllRolesAssignedToUserByUserId(String userName) throws UnsupportedEncodingException {
        String url = String.format(API.USERS_GET_ROLES.getUrl(), URLEncoder.encode(userName + "@" + PARTITION, StandardCharsets.UTF_8.name()));
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .get(url);

//        return SystemUtil.readJSONField(http.getResponse(), "users");
        return http.getResponse();
    }

    public String assignUserToRoleByUserId(String userName, ArrayList<String> roles) throws UnsupportedEncodingException, JsonProcessingException {
        String url = String.format(API.USERS_ASSIGN_ROLE.getUrl(), URLEncoder.encode(userName + "@" + PARTITION, StandardCharsets.UTF_8.name()));

        String rolesStr = new ObjectMapper().writeValueAsString(roles);

        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .setData("{" +
                        "\"stringList\": " +
                        rolesStr +
                        "}")
                .post(url);
        return http.getResponse();
    }

    class CustomAttribute {
        private String type;
        private String name;
        private String value;

        public CustomAttribute(String type, String name, String value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws JsonProcessingException, UnsupportedEncodingException {
        Otds otds = new Otds("Admin", "Asset99a");

        System.out.println(Otds.login("Admin", "Asset99a"));
    }
}
