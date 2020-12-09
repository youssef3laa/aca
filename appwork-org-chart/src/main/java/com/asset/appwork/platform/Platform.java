package com.asset.appwork.platform;

import com.asset.appwork.util.Http;
import com.asset.appwork.utils.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import static com.asset.appwork.otds.Otds.login;

public final class Platform {
    public static final String HOST = "appworks-aca";
    public static final String PORT = "8080";
    public static final String ORGANIZATION = "system";
    public static final String API_BASE_URL = "http://" + HOST + ":" + PORT + "/home/" + ORGANIZATION + "/app/entityRestService/api/OpenTextEntityIdentityComponents";
    public static final String GATEWAY_URL = "http://" + HOST + ":" + PORT + "/home/" + ORGANIZATION + "/com.eibus.web.soap.Gateway.wcp";

//    private String ticket = "";
    private String samlArt = "";

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
        POSITION_UPDATE("/entities/OrganizationalUnit/items/%s/childEntities/Position/items/%s"),
        ;

        private final String url;
        API(String url) {
            this.url = API_BASE_URL + url;
        }

        public String getUrl() {
            return this.url;
        }
    }

    public Platform(String username, String password) throws IOException {
        this(login(username, password));
    }
    public Platform(String ticket) throws IOException {
//        this.ticket = ticket;
        this.samlArt = getSAMLart(ticket);

    }

    public String getSAMLart(String ticket) throws IOException {
        String request = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "    <SOAP:Header>" +
                "        <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">" +
                "            <AuthenticationToken>" +
                    ticket +
                "            </AuthenticationToken>" +
                "        </OTAuthentication>" +
                "    </SOAP:Header>" +
                "    <SOAP:Body>" +
                "        <samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\" MajorVersion=\"1\" MinorVersion=\"1\" IssueInstant=\"2018-09-07T16:47:13.359Z\" RequestID=\"a5470c392e-264e-jopl-56ac-4397b1b416d\">" +
                "            <samlp:AuthenticationQuery>" +
                "                <saml:Subject xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\">" +
                "                    <saml:NameIdentifier Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\"/>" +
                "                </saml:Subject>" +
                "            </samlp:AuthenticationQuery>" +
                "        </samlp:Request>" +
                "    </SOAP:Body>" +
                "</SOAP:Envelope>";

        Http http = new Http().setContentType(Http.ContentType.XML_REQUEST)
                .setData(request)
                .post(GATEWAY_URL);
        return JacksonUtil.getJsonFieldByPath(JacksonUtil.XmlToJsonString(http.getResponse()), "/Body/Response/AssertionArtifact");
    }

    public String createOrgUnit(String name, String description) {
        String url = API.ORGUNITS_CREATE.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"Name\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Type\": \"Hierarchical\"" +
                        "  }" +
                        "}")
                .post(url);
        return http.getResponse();
    }
    public String createOrgUnit(String name) {
        return createOrgUnit(name, "");
    }

    public String readOrgUnit(Integer id){
        String url = String.format(API.ORGUNITS_GET.getUrl(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .get(url);
        return http.getResponse();
    }

    public String updateOrgUnit(Integer id, String name, String description) {
        String url = String.format(API.ORGUNITS_UPDATE.getUrl(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"Name\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Type\": \"Hierarchical\"" +
                        "  }" +
                        "}")
                .put(url);
        return http.getResponse();
    }

    public String deleteOrgUnit(Integer id) {
        String url = String.format(API.ORGUNITS_DELETE.getUrl(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .delete(url);
        return http.getResponse();
    }

    public String getAllOrgUnit() throws IOException {
        String url = API.ORGUNITS_GET_ALL.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("{}")
                .post(url);
        return http.getResponse();
    }

    public String createPosition(Integer orgUnitId, String name, String description, Boolean isLead) {
        String url = String.format(API.POSITION_CREATE.getUrl(), orgUnitId.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"PositionName\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Lead\": " + isLead.toString() +
                        "  }" +
                        "}")
                .post(url);
        return http.getResponse();
    }
    public String createPosition(Integer orgUnitId, String name, String description) {
        return createPosition(orgUnitId, name, description, false);
    }
    public String createPosition(Integer orgUnitId, String name, Boolean isLead) {
        return createPosition(orgUnitId, name, "", isLead);
    }
    public String createPosition(Integer orgUnitId, String name) {
        return createPosition(orgUnitId, name, "", false);
    }

    public String readPosition(Integer orgUnitId, Integer id){
        String url = String.format(API.POSITION_GET.getUrl(), orgUnitId.toString(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .get(url);
        return http.getResponse();
    }

    public String updatePosition(Integer orgUnitId, Integer id, String name, String description, Boolean isLead) {
        String url = String.format(API.POSITION_UPDATE.getUrl(), orgUnitId.toString(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"PositionName\": \"" + name + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Lead\": " + isLead.toString() +
                        "  }" +
                        "}")
                .put(url);
        return http.getResponse();
    }

    public String deletePosition(Integer orgUnitId, Integer id) {
        String url = String.format(API.POSITION_DELETE.getUrl(), orgUnitId.toString(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .delete(url);
        return http.getResponse();
    }

    public String getAllPosition(){
        String url = API.POSITION_GET_ALL.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("{}")
                .post(url);
        return http.getResponse();
    }

    public String getAllPersons(){
        String url = API.PERSON_GET_ALL.getUrl();
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("{}")
                .post(url);
        return http.getResponse();
    }

    public String createAssignment(Integer orgUnitId, Integer positionId) {
        String url = String.format(API.ASSIGNMENT_CREATE.getUrl(), orgUnitId.toString(), positionId.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
//                .setData(new ObjectMapper().writeValueAsString(assignments))
                .setData("{}")
                .post(url);
        return http.getResponse();
    }

    public String deleteAssignment(Integer id) {
        String url = String.format(API.ASSIGNMENT_DELETE_SELF.getUrl(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .delete(url);
        return http.getResponse();
    }

    public String addPersonToAssignment(Integer id, Integer personId) throws JsonProcessingException {
        String url = String.format(API.ASSIGNMENT_ADD_TO.getUrl(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData("  {" +
                        "    \"targetId\": " + "\"" + personId.toString() + "\"" +
                        "  }")
                .put(url);
        return http.getResponse();
    }

    public String addSubUnitToUnit(Integer orgUnitId, ArrayList<IdentityTargetId> ids) throws JsonProcessingException {
        String url = String.format(API.ORGUNITS_ADD_SUB_UNITS.getUrl(), orgUnitId.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .setData(new ObjectMapper().writeValueAsString(ids))
                .put(url);
        return http.getResponse();
    }

    public String clearSubUnitFromOrgUnit(Integer orgUnitId, Integer id) {
        String url = String.format(API.ORGUNITS_CLEAR_SUB_UNIT.getUrl(), orgUnitId.toString(), id.toString()) + "?deleteTarget=false";
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .delete(url);
        return http.getResponse();
    }

    public String getSubUnitsInOrgUnit(Integer id){
        String url = String.format(API.ORGUNITS_GET_SUB_UNITS.getUrl(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
                .setHeader("SAMLart", samlArt)
                .get(url);
        return http.getResponse();
    }

    static class IdentityTargetId {
        String targetId;

        public IdentityTargetId(Integer targetId) {
            this.targetId = targetId.toString();
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }
    }

    public static void main(String[] args) throws IOException {
    }
}
