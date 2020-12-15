package com.asset.appwork.cs;

import com.asset.appwork.dto.DocumentQuery;
import com.asset.appwork.util.Http;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private static final String CS_HOST = "http://cs/otcs/cs.exe/";
    private static final String API_VER = "api/v2/";
    private static final String API_ROOT = "nodes/";
    private final String userName;
    private final String password;


    public AppworkCSOperations(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //upload, versioning, search, download, brava, viewing, delete, rename, creation folder
    public CSResponse getNodeDetails(String nodeId) {
        String urlStr = CS_API.NODE_ACTION.getApiURL() + "/" +
                nodeId;
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(urlStr);
        return new CSResponse(http.getResponse(), http.getStatusCode());
    }

    public CSResponse uploadDocument(byte[] file, String parentId, String fileName, String type) {
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
        return new CSResponse(http.getResponse(), http.getStatusCode());
    }


    public CSResponse getSubNodes(String parentId, DocumentQuery documentQuery) throws JsonProcessingException {
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
        return new CSResponse(http.getResponse(), http.getStatusCode());
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

    public static class CSResponse {
        private final String response;
        private final Integer statusCode;

        public CSResponse(String response, Integer statusCode) {
            this.response = response;
            this.statusCode = statusCode;
        }

        public String getResponse() {
            return response;
        }

        public Integer getStatusCode() {
            return statusCode;
        }
    }


}



