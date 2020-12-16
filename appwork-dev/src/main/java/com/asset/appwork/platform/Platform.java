package com.asset.appwork.platform;

import com.asset.appwork.util.Http;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Platform {
    String apiBaseUrl;
    String SAMLart;

    public Platform(String apiBaseUrl, String SAMLart) {
        this.SAMLart = SAMLart;
        this.apiBaseUrl = apiBaseUrl;
    }

    public static void main(String[] args) throws IOException {
        Platform plat = new Platform("admin", "Asset99a");
        System.out.println(plat.deleteOrgUnit(49154L));
    }

    public String createOrgUnit(String name, String description) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"Name\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Type\": \"Hierarchical\"" +
                        "  }" +
                        "}")
                .post(this.apiBaseUrl + API.ORGUNITS_CREATE.getUrl());
        return http.getResponse();
    }

    public String createOrgUnit(String name) {
        return createOrgUnit(name, "");
    }

    public String readOrgUnit(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .get(this.apiBaseUrl + String.format(API.ORGUNITS_GET.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String updateOrgUnit(Long id, String name, String description) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"Name\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Type\": \"Hierarchical\"" +
                        "  }" +
                        "}")
                .put(this.apiBaseUrl + String.format(API.ORGUNITS_UPDATE.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String deleteOrgUnit(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .delete(this.apiBaseUrl + String.format(API.ORGUNITS_DELETE.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String getAllOrgUnit() throws IOException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{}")
                .post(this.apiBaseUrl + API.ORGUNITS_GET_ALL.getUrl());
        return http.getResponse();
    }

    public String createPosition(Long orgUnitId, String name, String description, Boolean isLead) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"PositionName\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Lead\": " + isLead.toString() +
                        "  }" +
                        "}")
                .post(this.apiBaseUrl + String.format(API.POSITION_CREATE.getUrl(), orgUnitId.toString()));
        return http.getResponse();
    }

    public String createPosition(Long orgUnitId, String name, String description) {
        return createPosition(orgUnitId, name, description, false);
    }

    public String createPosition(Long orgUnitId, String name, Boolean isLead) {
        return createPosition(orgUnitId, name, "", isLead);
    }

    public String createPosition(Long orgUnitId, String name) {
        return createPosition(orgUnitId, name, "", false);
    }

    public String readPosition(Long orgUnitId, Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .get(this.apiBaseUrl + String.format(API.POSITION_GET.getUrl(), orgUnitId.toString(), id.toString()));
        return http.getResponse();
    }

    public String updatePosition(Long orgUnitId, Long id, String name, String description, Boolean isLead) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"PositionName\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Lead\": " + isLead.toString() +
                        "  }" +
                        "}")
                .put(this.apiBaseUrl + String.format(API.POSITION_UPDATE.getUrl(), orgUnitId.toString(), id.toString()));
        return http.getResponse();
    }

    public String deletePosition(Long orgUnitId, Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .delete(this.apiBaseUrl + String.format(API.POSITION_DELETE.getUrl(), orgUnitId.toString(), id.toString()));
        return http.getResponse();
    }

    public String getAllPosition() {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{}")
                .post(this.apiBaseUrl + API.POSITION_GET_ALL.getUrl());
        return http.getResponse();
    }

    public String getAllPersons() {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{}")
                .post(this.apiBaseUrl + API.PERSON_GET_ALL.getUrl());
        return http.getResponse();
    }

    public String createAssignment(Long orgUnitId, Long positionId) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{}")
                .post(this.apiBaseUrl + String.format(API.ASSIGNMENT_CREATE.getUrl(), orgUnitId.toString(), positionId.toString()));
        return http.getResponse();
    }

    public String deleteAssignment(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .delete(this.apiBaseUrl + String.format(API.ASSIGNMENT_DELETE_SELF.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String addPersonToAssignment(Long id, Long personId) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("  {" +
                        "    \"targetId\": " + "\"" + personId.toString() + "\"" +
                        "  }")
                .put(this.apiBaseUrl + String.format(API.ASSIGNMENT_ADD_TO.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String addSubUnitToUnit(Long orgUnitId, List<IdentityTargetId> ids) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData(new ObjectMapper().writeValueAsString(ids))
                .put(this.apiBaseUrl + String.format(API.ORGUNITS_ADD_SUB_UNITS.getUrl(), orgUnitId.toString()));
        return http.getResponse();
    }

    public String clearSubUnitFromOrgUnit(Long orgUnitId, Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .delete(this.apiBaseUrl + String.format(API.ORGUNITS_CLEAR_SUB_UNIT.getUrl(), orgUnitId.toString(), id.toString()) + "?deleteTarget=false");
        return http.getResponse();
    }

    public String getSubUnitsInOrgUnit(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .get(this.apiBaseUrl + String.format(API.ORGUNITS_GET_SUB_UNITS.getUrl(), id.toString()));
        return http.getResponse();
    }

    private enum API {
        ASSIGNMENT_ADD_TO("/entities/Assignment/items/%s/relationships/toPersonToOne"),
        ASSIGNMENT_CREATE("/entities/OrganizationalUnit/items/%s/childEntities/Position/items/%s/relationships/ToAssignment"),
        ASSIGNMENT_DELETE("/entities/OrganizationalUnit/items/%s/childEntities/Position/items/%s/relationships/ToAssignment/targets/%s"),
        ASSIGNMENT_DELETE_SELF("/entities/Assignment/items/%s"),
        ORGUNITS_ADD_SUB_UNITS("/entities/OrganizationalUnit/items/%s/relationships/SubUnits"),
        ORGUNITS_CLEAR_SUB_UNIT("/entities/OrganizationalUnit/items/%s/relationships/SubUnits/targets/%s"),
        ORGUNITS_GET_SUB_UNITS("/entities/OrganizationalUnit/items/%s/relationships/SubUnits"),
        ORGUNITS_CREATE("/entities/OrganizationalUnit"),
        ORGUNITS_DELETE("/entities/OrganizationalUnit/items/%s"),
        ORGUNITS_GET("/entities/OrganizationalUnit/items/%s"),
        ORGUNITS_GET_ALL("/entities/OrganizationalUnit/lists/OrganizationalListInternal"),
        ORGUNITS_UPDATE("/entities/OrganizationalUnit/items/%s"),
        PERSON_GET_ALL("/entities/Person/lists/PersonListInternal"),
        POSITION_CREATE("/entities/OrganizationalUnit/items/%s/childEntities/Position"),
        POSITION_DELETE("/entities/OrganizationalUnit/items/%s/childEntities/Position/items/%s"),
        POSITION_GET("/entities/OrganizationalUnit/items/%s/childEntities/Position/items/%s"),
        POSITION_GET_ALL("/entities/OrganizationalUnit/childEntities/Position/lists/PositionListInternal"),
        POSITION_UPDATE("/entities/OrganizationalUnit/items/%s/childEntities/Position/items/%s");

        private String url;

        API(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    }

    public static class IdentityTargetId {
        String targetId;

        public IdentityTargetId(Long targetId) {
            this.targetId = targetId.toString();
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }
    }
}
