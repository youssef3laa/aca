package com.asset.appwork.config;


import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import com.auth0.jwt.JWT;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


/**
 * Created by omar.sabry on 11/8/2020.
 */
@Service
public class TokenService {

    private static final String SECRET = "Asset99a";
    //    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public String generate(Account account) throws AppworkException {
        try {
            ObjectMapper mapper =  new ObjectMapper();

            String jsonSubject = mapper.writeValueAsString(account);
            String token = JWT.create()
                    .withSubject(jsonSubject)
//            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(HMAC512(SECRET.getBytes()));
            return token;
        } catch (Exception e){
            throw new AppworkException(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }


    public Account get(String token) throws AppworkException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonSubject = JWT.require(HMAC512(SECRET.getBytes())).build().verify(token).getSubject();
            Account account = mapper.readValue(jsonSubject,Account.class);
            return account;
        }catch (Exception e){
            throw new AppworkException(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}