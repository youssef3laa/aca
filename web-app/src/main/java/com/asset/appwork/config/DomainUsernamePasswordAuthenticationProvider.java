package com.asset.appwork.config;

import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.exception.AppworkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by omar.sabry on 11/8/2020.
 */
public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    CordysManagement cordysManagement;

    private TokenService tokenService;

    public DomainUsernamePasswordAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional username = (Optional) authentication.getPrincipal();
        Optional password = (Optional) authentication.getCredentials();

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid Domain User Credentials");
        }

        try {
            CordysManagement.User user = cordysManagement.getUser((String)username.get(), (String)password.get(), "mod");

            Account account = new Account((String)username.get(),(String)password.get(),"mod",user.getSAMLart());

            AuthenticationWithToken resultOfAuthentication = new AuthenticationWithToken(username, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));

            String newToken = tokenService.generateNewToken(account);
            resultOfAuthentication.setToken(newToken);
//            tokenService.store(newToken, resultOfAuthentication);

            return resultOfAuthentication;
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