package com.asset.appwork.config;


import com.asset.appwork.dto.Account;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.concurrent.ConcurrentHashMap;
import com.auth0.jwt.JWT;
import org.springframework.security.core.authority.AuthorityUtils;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


/**
 * Created by omar.sabry on 11/8/2020.
 */
public class TokenService {

//    private static final ConcurrentHashMap<String, Object> restApiAuthTokenCache = new ConcurrentHashMap<String, Object>();
    private static final String SECRET = "Asset99a";
    //    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public String generateNewToken(Account account) {
        try {
            ObjectMapper mapper =  new ObjectMapper();

            String jsonSubject = mapper.writeValueAsString(account);
            String token = JWT.create()
                    .withSubject(jsonSubject)
//            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(HMAC512(SECRET.getBytes()));
            return token;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Authentication retrieve(String token) {
        try {
            Account account = readTokenData(token);
            if(account == null){
                return null;
            }
            AuthenticationWithToken resultOfAuthentication = new AuthenticationWithToken(account.getUsername(), null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
            resultOfAuthentication.setToken(token);
            return resultOfAuthentication;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Account readTokenData(String token){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonSubject = JWT.require(HMAC512(SECRET.getBytes())).build().verify(token).getSubject();
            Account account = mapper.readValue(jsonSubject,Account.class);
//            if(account.isPresent()){
            return account;
//            }else{
//                return null;
//            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

//    public boolean contains(String token) {
//        return restApiAuthTokenCache.get(token) != null;
//    }

//    public void store(String token, Authentication authentication) {
//        restApiAuthTokenCache.put(token, authentication);
//    }

//    public Authentication retrieve(String token) {
//        return (Authentication) restApiAuthTokenCache.get(token);
//    }
}