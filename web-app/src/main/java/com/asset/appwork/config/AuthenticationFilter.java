package com.asset.appwork.config;

import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by omar.sabry on 11/8/2020.
 */
public class AuthenticationFilter extends GenericFilterBean{
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

//        Optional<String> username = Optional.ofNullable(httpRequest.getHeader("X-Auth-Username"));
//        Optional<String> password = Optional.ofNullable(httpRequest.getHeader("X-Auth-Password"));
        Optional<String> token = Optional.ofNullable(httpRequest.getHeader("X-Auth-Token"));

        try {

//            if (username.isPresent() && username.isPresent()){
//                processUsernamePasswordAuthentication(httpResponse, username, password);
//                return;
//            }
//            else
            if (token.isPresent() && !((String)token.get()).isEmpty()) {
                processTokenAuthentication(token);
            }
//            else {
//                processUsernamePasswordAuthentication(httpResponse, username, password);
//                return;
//            }
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

//    private void processUsernamePasswordAuthentication(HttpServletResponse httpResponse, Optional username, Optional password) throws IOException {
//        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
//        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
//        httpResponse.setStatus(HttpServletResponse.SC_OK);
//        String tokenJsonResponse = new ObjectMapper().writeValueAsString(resultOfAuthentication.getDetails().toString());
//        httpResponse.addHeader("Content-Type", "application/json");
//        httpResponse.getWriter().print(tokenJsonResponse);
//    }

//    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional username, Optional password) {
//        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
//        return tryToAuthenticate(requestAuthentication);
//    }

    private void processTokenAuthentication(Optional<String> token) {
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken
                requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        return responseAuthentication;
    }


    public static class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {

        public AuthenticationWithToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
            super(aPrincipal, aCredentials, anAuthorities);
        }

        public void setToken(String token) {
            setDetails(token);
        }

        public Object getToken(){
            return getDetails();
        }

    }

//    static class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {
//
//        @Autowired
//        CordysManagement cordysManagement;
//
//        private TokenService tokenService;
//
//        public DomainUsernamePasswordAuthenticationProvider(TokenService tokenService) {
//            this.tokenService = tokenService;
//        }
//
//        @SneakyThrows
//        @Override
//        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//            Optional username = (Optional) authentication.getPrincipal();
//            Optional password = (Optional) authentication.getCredentials();
//
//            if (!username.isPresent() || !password.isPresent()) throw new BadCredentialsException("Invalid Domain User Credentials");
//
//            CordysManagement.User user = cordysManagement.getUser((String)username.get(), (String)password.get(), "mod");
//
//            Account account = new Account((String)username.get(),(String)password.get(),"mod",user.getSAMLart());
//
//            AuthenticationFilter.AuthenticationWithToken resultOfAuthentication = new AuthenticationFilter.AuthenticationWithToken(username, null,
//                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
//
//            String newToken = tokenService.generateNewToken(account);
//                resultOfAuthentication.setToken(newToken);
//
//            return resultOfAuthentication;
//
//        }
//
//        @Override
//        public boolean supports(Class<?> authentication) {
//            return authentication.equals(UsernamePasswordAuthenticationToken.class);
//        }
//    }


    static class TokenAuthenticationProvider implements AuthenticationProvider {

        private TokenService tokenService;

        public TokenAuthenticationProvider(TokenService tokenService) {
            this.tokenService = tokenService;
        }

        @SneakyThrows
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            Optional token = (Optional) authentication.getPrincipal();
            if (!token.isPresent()) throw new BadCredentialsException("Invalid token");

            Authentication tokenAuthentication = tokenService.retrieve((String) token.get());
            if(tokenAuthentication == null) throw new BadCredentialsException("Invalid token or token expired");
            return tokenAuthentication;

        }

        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(PreAuthenticatedAuthenticationToken.class);
        }
    }
}
