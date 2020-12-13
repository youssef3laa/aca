package com.asset.appwork.cs;

import com.asset.appwork.util.Http;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class AppworkCSOperations {

    // content server host

    private static final String CS_HOST = "http://cs/otcs/cs.exe/";
    private final String userName;
    private final String password;


    public AppworkCSOperations(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //upload, versioning, search, download, brava, viewing, delete, rename, creation folder
    public String getNodeDetails(String nodeId) {
        String urlStr = CS_HOST + CS_API.NODE_DETAILS.getApiURL() +
                nodeId;
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(urlStr);
        return http.getResponse();
    }

    public String uploadDocument(byte[] file, String parentId, String fileName, String type) {
        String urlStr = CS_API.UPLOAD_NODE.getApiURL();
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
        return http.getResponse();
    }

    public enum CS_API {
        //APIs URL
        //https://appworksdeveloper.opentext.com/webaccess/#url=%2Fawd%2Fresources%2Fapis%2Fcs-rest-api-for-cs-16-s%23!%2Fnodes&tab=501

        //api/v2/nodes/{id}
        NODE_DETAILS("api/v2/nodes/"),
        UPLOAD_NODE("api/v2/nodes");

        private final String apiURL;

        CS_API(String s) {
            this.apiURL = CS_HOST + s;
        }

        public String getApiURL() {
            return apiURL;
        }
    }


}



