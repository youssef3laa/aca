package com.asset.appwork.service;

import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

/**
 * Created by karim on 10/26/20.
 */
@Service
public class CordysService {

    public String login(String username, String passowrd) throws JsonProcessingException {
        String otdsURL = "http://appworks-dev:8080/otdsws/rest/authentication/credentials";

        String data = "{\"userName\" : \""+username+"\", \"password\" : \"" + passowrd + "\" }";
        Http http = new Http().setDoAuthentication(true).setContentType(Http.ContentType.JSON_REQUEST).setData(data).post(otdsURL);
        return SystemUtil.readJSONField(http.getResponse(), "ticket");
    }
}
