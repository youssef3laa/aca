package com.asset.appwork.platform.rest;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;

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

    public <T> Long create(T data) throws AppworkException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": " +
                        data.toString() +
                        "}")
                .post(this.apiBaseUrl + String.format(API.ADD.getUrl(), this.entityName));
        String response = http.getResponse();
        try {
            return Long.parseLong(SystemUtil.getJsonByPtrExpr(response, "/Identity/Id"));
        } catch (NumberFormatException e) {
            try {
                throw new AppworkException(SystemUtil.getJsonByPtrExpr(response, "/message"), ResponseCode.CREATE_ENTITY_FAILURE);
            } catch (AppworkException er) {
                throw new AppworkException(er.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR);
            }
        }
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

    public <T> String readList(String listName, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData(data.toString())
                .post(this.apiBaseUrl + String.format(API.LIST.getUrl(), this.entityName, listName));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String update(Long id, T data) {
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

    public <T> Long createChild(Long parentId, String childName, T data) throws AppworkException {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData("{" +
                        "  \"Properties\": " +
                        data.toString() +
                        "}")
                .post(this.apiBaseUrl + String.format(API.ADD_CHILD.getUrl(), this.entityName, parentId.toString(), childName));
        String response = http.getResponse();
        try {
            return Long.parseLong(SystemUtil.getJsonByPtrExpr(response, "/Identity/Id"));
        } catch (NumberFormatException e) {
            throw new AppworkException(SystemUtil.getJsonByPtrExpr(response, "/message"), ResponseCode.CREATE_ENTITY_FAILURE);
        }
    }

    public String readChildById(Long parentId, String childName, Long childId) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .get(this.apiBaseUrl + String.format(API.CHILD_ITEM.getUrl(), this.entityName, parentId.toString(), childName, childId.toString()));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String updateChild(Long parentId, String childName, Long childId, T data) {
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

    public <T> String addRelation(Long parentId, String relationName, T data) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .setData(data.toString())
                .put(this.apiBaseUrl + String.format(API.ONE_RELATION.getUrl(), this.entityName, parentId.toString(), relationName));
        return http.getResponse();
    }

    public <T> String readToManyRelation(Long parentId, String relationName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .get(this.apiBaseUrl + String.format(API.ONE_RELATION.getUrl(), this.entityName, parentId.toString(), relationName));
        return new String(http.getResponse().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public <T> String deleteToOneRelation(Long parentId, String relationName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .delete(this.apiBaseUrl + String.format(API.ONE_RELATION.getUrl(), this.entityName, parentId.toString(), relationName));
        return http.getResponse();
    }

    public <T> String deleteToManyRelation(Long parentId, String relationName) {
        Http http = new Http().setContentType(Http.ContentType.JSON_REQUEST)
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("SAMLart", this.account.getSAMLart())
                .delete(this.apiBaseUrl + String.format(API.MANY_RELATION.getUrl(), this.entityName, parentId.toString(), relationName));
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
        ONE_RELATION("/entities/%s/items/%s/relationships/%s"),
        MANY_RELATION("/entities/%s/items/%s/relationships/%s/targets/%s");

        private final String url;

        API(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    }
}
