package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.platform.util.CordysUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by karim on 10/26/20.
 */
@Service
public class CordysService {

    @Autowired
    Environment environment;

//    env.getProperty(organization+".domain") + env.getProperty(organization+".gateway.url");

    public String sendRequest(String message){

        String cordyUrl = environment.getProperty("server.request")+"://"+environment.getProperty("appwork.domain")+":"+environment.getProperty("appwork.port")+"/home/"+environment.getProperty("appwork.organization")+"/"+environment.getProperty("appwork.gateway");
        CordysUtil cordysUtil =new CordysUtil(cordyUrl);
        return cordysUtil.sendRequest(message);
    }


    public String sendRequest(Account account, String message) throws JsonProcessingException, AppworkException {
        String cordyUrl = environment.getProperty("server.request")+"://"+environment.getProperty("appwork.domain")+":"+environment.getProperty("appwork.port")+"/home/"+environment.getProperty("appwork.organization")+"/"+environment.getProperty("appwork.gateway");
        CordysUtil cordysUtil = new CordysUtil(cordyUrl);
        return cordysUtil.sendRequest(account, message);
    }

    public String getCordysUrl(){
        return environment.getProperty("server.request")+"://"+environment.getProperty("appwork.domain")+":"+environment.getProperty("appwork.port")+"/home/"+environment.getProperty("appwork.organization")+"/"+environment.getProperty("appwork.gateway");
    }

}
