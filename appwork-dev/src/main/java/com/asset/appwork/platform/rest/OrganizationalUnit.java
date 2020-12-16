package com.asset.appwork.platform.rest;

import com.asset.appwork.platform.Platform;
import com.asset.appwork.util.Http;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class OrganizationalUnit {

    String apiBaseUrl;
    String SAMLart;

    public OrganizationalUnit(String apiBaseUrl, String SAMLart) {
        this.SAMLart = SAMLart;
        this.apiBaseUrl = apiBaseUrl;
    }



    public String create(String nameAr, String description, String nameEn, String unitTypeCode, String unitCode) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"Name\": \"" + nameAr + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Type\": \"Hierarchical\"," +
                        "    \"Name_en\": \"" + nameEn + "\"," +
                        "    \"UnitTypeCode\": \"" + unitTypeCode + "\"," +
                        "    \"UnitCode\": \"" + unitCode + "\"" +
                        "  }" +
                        "}")
                .post(this.apiBaseUrl + API.ADD.getUrl());
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String create(String nameAr, String nameEn, String unitTypeCode, String unitCode) throws UnsupportedEncodingException {
        return create(nameAr, "", nameEn, unitTypeCode, unitCode);
    }

    public String read(Long id) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .get(this.apiBaseUrl + String.format(API.ITEM.getUrl(), id.toString()));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String update(Long id, String nameAr, String description, String nameEn, String unitTypeCode, String unitCode) throws UnsupportedEncodingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"Name\": \"" + nameAr + "\"," +
                        "    \"Description\": \"" + description + "\"," +
                        "    \"Type\": \"Hierarchical\"," +
                        "    \"Name_en\": \"" + nameEn + "\"," +
                        "    \"UnitTypeCode\": \"" + unitTypeCode + "\"," +
                        "    \"UnitCode\": \"" + unitCode + "\"" +
                        "  }" +
                        "}")
                .put(this.apiBaseUrl + String.format(API.ITEM.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String update(Long id, String unitCode) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{" +
                        "  \"Properties\": {" +
                        "    \"UnitCode\": \"" + unitCode + "\"" +
                        "  }" +
                        "}")
                .put(this.apiBaseUrl + String.format(API.ITEM.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String delete(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .delete(this.apiBaseUrl + String.format(API.ITEM.getUrl(), id.toString()));
        return http.getResponse();
    }

    public String getAll() {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData("{}")
                .post(this.apiBaseUrl + API.INTERNAL_LIST.getUrl());
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String toOneRelation(Long unitId, RELATION relation, Platform.IdentityTargetId relationId) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.SAMLart)
                .setData(new ObjectMapper().writeValueAsString(relationId))
                .put(this.apiBaseUrl + String.format(API.TO_ONE_RELATION.getUrl(), unitId.toString(), relation.getAsString()));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    private enum API {
        ORGUNITS_CLEAR_SUB_UNIT("/entities/OrganizationalUnit/items/%s/relationships/SubUnits/targets/%s"),

        ADD("/entities/OrganizationalUnit"),
        ITEM("/entities/OrganizationalUnit/items/%s"),
        INTERNAL_LIST("/entities/OrganizationalUnit/lists/OrganizationalListInternal"),
        TO_ONE_RELATION("/entities/OrganizationalUnit/items/%s/relationships/%s"),
        TO_MANY_RELATION("/entities/OrganizationalUnit/items/%s/relationships/%s/targets/%s"),
        ;

        private final String url;

        API(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    }

    private enum RELATION {
        SUBUNITS("SubUnits"),
        ;

        private final String relation;

        RELATION(String relation) {
            this.relation = relation;
        }

        public String getAsString() {
            return this.relation;
        }
    }


    public static void main(String[] args) throws IOException {
        // Added role Identity Administrator and Identity Push Service temporary to admin user
        OrganizationalUnit unit = new OrganizationalUnit("admin", "Asset99a");
//        System.out.println(unit.create("مكتب سوهاج", "Sohag Office", "SOO"));
        System.out.println(unit.getAll());
    }
}
