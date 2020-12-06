package com.asset.appwork.platform;

import com.asset.appwork.util.Http;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

import static com.asset.appwork.otds.Otds.login;

public final class Platform {
    public static final String HOST = "appworks-aca";
    public static final String PORT = "8080";
    public static final String ORGANIZATION = "system";
    public static final String API_BASE_URL = "http://" + HOST + ":" + PORT + "/home/" + ORGANIZATION + "/app/entityservice/OpenTextEntityIdentityComponents";

    private String ticket = "";

    private enum API {
        ORGUNITS_CREATE("/entities/OrganizationalUnit"),
        ORGUNITS_DELETE("/entities/OrganizationalUnit/items/%s"),
        ORGUNITS_GET("/entities/OrganizationalUnit/items/%s"),
        ORGUNITS_GET_ALL("/entities/OrganizationalUnit/lists/OrgUnitList"),
        ORGUNITS_UPDATE("/entities/OrganizationalUnit/items/%s");

        private final String url;
        API(String url) {
            this.url = API_BASE_URL + url;
        }

        public String getUrl() {
            return this.url;
        }
    }

    public Platform(String username, String password) throws JsonProcessingException {
        this(login(username, password));
    }
    public Platform(String ticket) throws JsonProcessingException {
        this.ticket = ticket;
    }

    public String createOrgUnit(String name, String description) {
        String url = API.ORGUNITS_CREATE.getUrl();
        System.out.println(url);
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setData("{}")
//                .setData("{" +
//                        "  \"Properties\": {" +
//                        "    \"Name\": \"" + name + "\"," +
//                        "    \"Description\": \"" + description + "\"," +
//                        "    \"Type\": \"Hierarchical\"" +
//                        "  }" +
//                        "}")
                .post(url);

        return http.getResponse();
    }
    public String createOrgUnit(String name) {
        return createOrgUnit(name, "");
    }

    public String readOrgUnit(Integer id){
        String url = String.format(API.ORGUNITS_GET.getUrl(), id.toString());
        Http http = new Http()
//                .setDoAuthentication(true)
//                .basicAuthentication(ADMIN_USERNAME, ADMIN_PASSWORD)
                .setHeader("OTDSTicket", ticket)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(url);
        return http.getResponse();
    }

    public String updateOrgUnit(Integer id, String newUnitName) {
        String url = String.format(API.ORGUNITS_UPDATE.getUrl(), id.toString());
        System.out.println(url);
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .put(url);
        return http.getResponse();
    }

    public String deleteOrgUnit(Integer id) {
        String url = String.format(API.ORGUNITS_DELETE.getUrl(), id.toString());
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("OTDSTicket", ticket)
                .delete(url);
        return http.getResponse();
    }

    public String getAllOrgUnit(){
        String url = API.ORGUNITS_GET_ALL.getUrl();
        System.out.println(url);
        System.out.println(ticket);
        Http http = new Http()
//                .setDoAuthentication(true)
//                .basicAuthentication("admin", "Asset99a")
//                .setHeader("OTAuthentication", "{" +
//                        "\"AuthenticationToken\": " + ticket +
//                        "}")
//                .setHeader("X-Requested-With", "XMLHttpRequest")
//                .setHeader("otdsticket", ticket)
//                .setHeader("SAMLart", "asdasdasdsda")
//                .setHeader("Content-Type", "text/plain")
                .setContentType(Http.ContentType.JSON_REQUEST)
                .setData("{" +
                        "}")
                .post(url);
//                .get(url);

        return http.getResponse();
    }

    public static void main(String[] args) throws IOException {
        Platform plat = new Platform("admin", "Asset99a");
        String response = plat.getAllOrgUnit();
        System.out.println("------------------------------------------");
        System.out.println(response);
        System.out.println("------------------------------------------");
    }
}
