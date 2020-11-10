package com.asset.appwork.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Optional;

/**
 * Created by omar.sabry on 11/8/2020.
 */
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private TokenService tokenService;

    public TokenAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        tokenService.store("1234",authentication);
        Optional token = (Optional) authentication.getPrincipal();
        if (!token.isPresent()) {
            throw new BadCredentialsException("Invalid token");
        }
        Authentication tokenAuthentication = tokenService.retrieve((String) token.get());
        if(tokenAuthentication == null){
            throw new BadCredentialsException("Invalid token or token expired");
        }
        return tokenAuthentication;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
