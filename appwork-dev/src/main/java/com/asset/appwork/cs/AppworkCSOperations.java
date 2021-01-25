package com.asset.appwork.cs;


import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AppworkCSOperations {

    // content server host
    private static final String CS_HOST = "http://45.240.63.94/otcs/cs.exe/";
    private static final String API_VER = "api/v2/";
    private static final String API_ROOT = "nodes/";
    private final String userName;
    private final String password;


    public AppworkCSOperations(String userName, String password) {
        //TODO use arguments
        this.userName = "admin";
        this.password = "Asset99a";
    }

    //document management
    public Http getNodeDetails(Long nodeId) throws AppworkException {
        String urlStr = CS_API.NODE_ACTION.getApiURL() + "/" +
                nodeId;
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(urlStr);
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }

    public Http deleteNode(Long nodeId) throws AppworkException {
        String urlStr = CS_API.NODE_ACTION.getApiURL() + "/" +
                nodeId;
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .delete(urlStr);
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }

    public Http uploadDocument(byte[] file, Long parentId, String fileName, String type) throws AppworkException {
        String urlStr = CS_API.NODE_ACTION.getApiURL();
        Part[] parts =
                {
                        new FilePart("file", new ByteArrayPartSource(fileName, file)),
                        new StringPart("name", fileName),
                        new StringPart("parent_id", String.valueOf(parentId)),
                        new StringPart("type", type)
                };

        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.FORM_REQUEST)
                .setData(parts)
                .post(urlStr);
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));

        return http;
    }

    public Http getSubNodes(Long parentId, DocumentQuery documentQuery) throws AppworkException, IllegalAccessException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_SUB_NODES.getApiURL());
        fillURIComponentWithQuery(uriComponentsBuilder, documentQuery);

        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", String.valueOf(parentId));
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());

        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }

    //versioning
    public Http getNodeVersions(Long nodeId, DocumentQuery documentQuery) throws IllegalAccessException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_NODES_VERSIONS.getApiURL());
        Class<DocumentQuery> cls = DocumentQuery.class;
        Field[] fields = cls.getDeclaredFields();
        String fieldName;
        for (Field field : fields) {
            fieldName = field.getName();
            JsonProperty[] jsonProperties = field.getAnnotationsByType(JsonProperty.class);
            if (jsonProperties.length > 0) fieldName = jsonProperties[0].value();

            uriComponentsBuilder.queryParamIfPresent(fieldName, Optional.ofNullable(field.get(documentQuery)));
        }
        System.out.println(uriComponentsBuilder);
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", nodeId);
        return new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());
    }

    public Http getSpecifiedNodeVersion(Long nodeId, Integer versionId, DocumentQuery documentQuery) throws IllegalAccessException, AppworkException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_SPECIFIED_NODE_VERSION.getApiURL());
        fillURIComponentWithQuery(uriComponentsBuilder, documentQuery);
        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("id", nodeId);
        pathVariables.put("version_number", versionId);
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }


    //categories
    public Http updateCategoryOnNode(Long nodeId, Long categoryId, Map<String, String> categoryValues) throws AppworkException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.UPDATE_CATEGORY_ON_NODE.getApiURL());
        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("id", nodeId);
        pathVariables.put("category_id", categoryId);
        Part[] parts = new Part[categoryValues.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : categoryValues.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
            parts[index] = new StringPart(entry.getKey(), entry.getValue());
            ++index;
        }
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setData(parts)
                .setContentType(Http.ContentType.FORM_REQUEST)
                .put(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());

        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));

        return http;
    }

    //general
    public void fillURIComponentWithQuery(UriComponentsBuilder uriComponentsBuilder, DocumentQuery documentQuery) throws IllegalAccessException {
        Class<DocumentQuery> cls = DocumentQuery.class;
        Field[] fields = cls.getDeclaredFields();
        String fieldName;
        for (Field field : fields) {
            fieldName = field.getName();
            JsonProperty[] jsonProperties = field.getAnnotationsByType(JsonProperty.class);
            if (jsonProperties.length > 0) fieldName = jsonProperties[0].value();
            uriComponentsBuilder.queryParamIfPresent(fieldName, Optional.ofNullable(field.get(documentQuery)));
        }
    }

    public Http downloadDocument(Long documentId) throws AppworkException {
        String urlStr = CS_API.NODE_ACTION.getApiURL() + documentId + "/content";
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .download(urlStr);
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));

        return http;
    }

    public enum CS_API {
        //APIs source URL
        //https://appworksdeveloper.opentext.com/webaccess/#url=%2Fawd%2Fresources%2Fapis%2Fcs-rest-api-for-cs-16-s%23!%2Fnodes&tab=501

        //api/v2/nodes/{id}
        /*
         * get node details
         * upload node
         * */
        NODE_ACTION(),
        //{{baseUrl}}/v2/nodes/:id/nodes
        GET_SUB_NODES("{id}/nodes"),

        //versions
        //{{baseUrl}}/v2/nodes/:id/versions
        GET_NODES_VERSIONS("{id}/versions"),
        //{{baseUrl}}/v2/nodes/:id/versions
        GET_SPECIFIED_NODE_VERSION("{id}/versions/{version_number}"),

        //categories
        UPDATE_CATEGORY_ON_NODE("{id}/categories/{category_id}");

        private final String apiURL;

        CS_API() {
            this.apiURL = CS_HOST + API_VER + API_ROOT;
        }

        CS_API(String s) {
            this.apiURL = CS_HOST + API_VER + API_ROOT + s;
        }

        public String getApiURL() {
            return apiURL;
        }

    }

    @Data
    public static class DocumentQuery {
        @JsonProperty("where_type")
        String whereType;

        String sort;
        String fields;
        String page;
        String limit;
        String order;
        String expand;

    }


}



