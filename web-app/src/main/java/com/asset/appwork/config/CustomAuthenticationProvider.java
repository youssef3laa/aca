package com.asset.appwork.config;

import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by omar.sabry on 11/8/2020.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    CordysManagement cordysManagement;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            CordysManagement.User user = cordysManagement.getUser(name, password, "MOD");
            return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
        } catch (JsonProcessingException e) {
            return null;
        } catch (AppworkException e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}



