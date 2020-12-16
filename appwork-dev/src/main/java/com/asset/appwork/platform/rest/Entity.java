package com.asset.appwork.platform.rest;

import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.orgchart.UserManagement;
import com.asset.appwork.platform.Platform;
import com.asset.appwork.util.Http;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Entity {

    Account account;
    String apiBaseUrl;
    String entityName;
//    API api;

    public Entity(Account account, String apiBaseUrl, String entityName) {
        this.account = account;
        this.apiBaseUrl = apiBaseUrl;
        this.entityName = entityName;
    }
//    public Entity setUrl(API api){
//        this.api = api;
//        return this;
//    }

    public <T> String create(T data){
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": {" +
                            data.toString() +
                        "  }" +
                        "}")
                .post(this.apiBaseUrl + String.format(API.ADD.getUrl(), this.entityName));
        return http.getResponse();
    }

    public String readById(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .get(this.apiBaseUrl + String.format(API.ITEM.getUrl(), this.entityName, id.toString()));
        return http.getResponse();
    }

    public String readList(String listName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{}")
                .post(this.apiBaseUrl + String.format(API.LIST.getUrl(), this.entityName, listName));
        return http.getResponse();
    }

    public <T> String update(Long id, T data){
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": {" +
                        data.toString() +
                        "  }" +
                        "}")
                .put(this.apiBaseUrl + String.format(API.ITEM.getUrl(), this.entityName, id.toString()));
        return http.getResponse();
    }

    public String delete(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .delete(this.apiBaseUrl + String.format(API.ITEM.getUrl(), this.entityName, id.toString()));
        return http.getResponse();
    }

    // Change IdentityTargetId to Long?
    public String addToOneRelation(Long groupId, String withEntity, Platform.IdentityTargetId relationId) throws JsonProcessingException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData(new ObjectMapper().writeValueAsString(relationId))
                .put(this.apiBaseUrl + String.format(API.TO_ONE_RELATION.getUrl(), this.entityName, groupId.toString(), withEntity));
        return http.getResponse();
    }

    private enum API {
        ADD("/entities/%s"),
        ITEM("/entities/%s/items/%s"),
        LIST("/entities/%s/lists/%s"),
        TO_ONE_RELATION("/entities/%s/items/%s/relationships/%s"),
        TO_MANY_RELATION("/entities/%s/items/%s/relationships/%s/targets/%s");

        private final String url;

        API(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    }

//    private enum RELATION {
//        POSITION("Position"),
//        ;
//
//        private final String relation;
//
//        RELATION(String relation) {
//            this.relation = relation;
//        }
//
//        public String getAsString() {
//            return this.relation;
//        }
//    }


    public static void main(String[] args) throws IOException, AppworkException {
        // Added role Identity Administrator and Identity Push Service temporary to admin user
        Entity entity = new Entity(new UserManagement().create("admin", "Asset99a"),
                "http://appworks-aca.local:8080/home/system/app/entityRestService/api/AssetACA/",
                "OrganizationalUnit");
        System.out.println(entity.readById(65538L));
    }

}
