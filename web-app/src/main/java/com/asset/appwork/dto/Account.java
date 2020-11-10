package com.asset.appwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.Authentication;

/**
 * Created by karim on 10/29/20.
 */
@Data
public class Account {

    public Account(){

    }

    public Account(String username,String password,String organization,String SAMLart){
        this.username = username;
        this.password = password;
        this.organization = organization;
        this.SAMLart = SAMLart;
    }

    String username;
    String password;
    String organization;
    @JsonProperty("SAMLart")
    String SAMLart;
}
