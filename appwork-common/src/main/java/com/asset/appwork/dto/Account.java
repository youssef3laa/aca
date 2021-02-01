package com.asset.appwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by karim on 10/29/20.
 */
@Data
public class Account {
    String username;
    String password;
    String organization;
    @JsonProperty("SAMLart")
    String SAMLart;
    String ticket;

    public Account(){

    }

    public Account(String username, String password, String organization, String ticket, String SAMLart){
        this.ticket = ticket;
        this.username = username;
        this.password = password;
        this.organization = organization;
        this.SAMLart = SAMLart;
    }



}
