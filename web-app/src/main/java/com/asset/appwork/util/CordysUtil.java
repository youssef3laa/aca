package com.asset.appwork.util;

import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by karim on 10/26/20.
 */
@Service
public class CordysUtil {
    @Autowired
    CordysManagement cordysManagement;
    public String sendRequest(Account account, String message) throws JsonProcessingException, AppworkException {
        CordysManagement.Cordys cordys = cordysManagement.getCordys(account.getOrganization());
        Http http = new Http().setDoAuthentication(true).setContentType(Http.ContentType.XML_REQUEST).setData(message).post(cordys.getUrl());
        // @TODO re-login
        return http.getResponse();
    }

}
