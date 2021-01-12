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

    //upload, versioning, search, download, brava, viewing, delete, rename, creation folder
    public Http getNodeDetails(String nodeId) throws AppworkException {
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

    public Http deleteNode(String nodeId) throws AppworkException {
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

    public Http uploadDocument(byte[] file, String parentId, String fileName, String type) throws AppworkException {
        String urlStr = CS_API.NODE_ACTION.getApiURL();
        Part[] parts =
                {
                        new FilePart("file", new ByteArrayPartSource(fileName, file)),
                        new StringPart("name", fileName),
                        new StringPart("parent_id", parentId),
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


    public Http getSubNodes(String parentId, DocumentQuery documentQuery) throws AppworkException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_SUB_NODES.getApiURL());
        uriComponentsBuilder.queryParamIfPresent("where_type", Optional.ofNullable(documentQuery.getWhereType()));
        uriComponentsBuilder.queryParamIfPresent("sort", Optional.ofNullable(documentQuery.getSort()));
        uriComponentsBuilder.queryParamIfPresent("fields", Optional.ofNullable(documentQuery.getFields()));

        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", parentId);
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());

        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }


    public enum CS_API {
        //APIs URL
        //https://appworksdeveloper.opentext.com/webaccess/#url=%2Fawd%2Fresources%2Fapis%2Fcs-rest-api-for-cs-16-s%23!%2Fnodes&tab=501

        //api/v2/nodes/{id}
        /*
         * get node details
         * upload node
         * */
        NODE_ACTION(),
        //{{baseUrl}}/v2/nodes/:id/nodes
        GET_SUB_NODES("{id}/nodes");

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

    }


}



