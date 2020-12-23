package com.asset.appwork.platform.rest;

import com.asset.appwork.dto.Account;
import com.asset.appwork.platform.Platform;
import com.asset.appwork.util.Http;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

public class Entity {

    Account account;
    String apiBaseUrl;
    String entityName;

    public Entity(Account account, String apiBaseUrl, String entityName) {
        this.account = account;
        this.apiBaseUrl = apiBaseUrl;
        this.entityName = entityName;
    }

    public <T> String create(T data){
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": " +
                                data.toString() +
                        "}")
                .post(this.apiBaseUrl + String.format(API.ADD.getUrl(), this.entityName));
        return http.getResponse();
    }

    public String readById(Long id) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .get(this.apiBaseUrl + String.format(API.ITEM.getUrl(), this.entityName, id.toString()));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String readList(String listName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{}")
                .post(this.apiBaseUrl + String.format(API.LIST.getUrl(), this.entityName, listName));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String update(Long id, T data){
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": " +
                                data.toString() +
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

    public <T> String createChild(Long parentId, String childName, T data){
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": " +
                        data.toString() +
                        "}")
                .post(this.apiBaseUrl + String.format(API.ADD_CHILD.getUrl(), this.entityName, parentId.toString(), childName));
        return http.getResponse();
    }

    public String readChildById(Long parentId, String childName, Long childId) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .get(this.apiBaseUrl + String.format(API.CHILD_ITEM.getUrl(), this.entityName, parentId.toString(), childName, childId.toString()));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String updateChild(Long parentId, String childName, Long childId, T data){
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": " +
                        data.toString() +
                        "}")
                .put(this.apiBaseUrl + String.format(API.CHILD_ITEM.getUrl(), this.entityName, parentId.toString(), childName, childId.toString()));
        return http.getResponse();
    }

    public String deleteChild(Long parentId, String childName, Long childId) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .delete(this.apiBaseUrl + String.format(API.CHILD_ITEM.getUrl(), this.entityName, parentId.toString(), childName, childId.toString()));
        return http.getResponse();
    }

    public String readChildItems(Long parentId, String childName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .get(this.apiBaseUrl + String.format(API.READ_CHILD_ITEMS.getUrl(), this.entityName, parentId.toString(), childName));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String readChildList(String childName, String listName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{}")
                .post(this.apiBaseUrl + String.format(API.READ_CHILD_ITEMS.getUrl(), this.entityName, childName, listName));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
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
        ADD_CHILD("/entities/%s/items/%s/childEntities/%s"),
        CHILD_ITEM("/entities/%s/items/%s/childEntities/%s/items/%s"),
        READ_CHILD_ITEMS("/entities/%s/items/%s/childEntities/%s"),
        LIST_CHILD("/entities/%s/childEntities/%s/lists/%s"),
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
}
