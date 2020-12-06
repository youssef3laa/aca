package com.asset.appwork.config;

import com.asset.appwork.dto.Account;
import com.asset.appwork.otds.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

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
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Optional<String> token = Optional.ofNullable(httpRequest.getHeader("X-Auth-Token"));

        try {
            if (token.isPresent() && !((String)token.get()).isEmpty()) {
                processTokenAuthentication(token);
            }

            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    public void processTokenAuthentication(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
    }


    public static class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {

        public AuthenticationWithToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
            super(aPrincipal, aCredentials, anAuthorities);
        }

        public void setToken(String token) {
            setDetails(token);
        }
    }

    @Service
    public static class TokenAuthenticationProvider implements AuthenticationProvider {
        @Autowired
        TokenService tokenService;

        @SneakyThrows
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            Optional token = (Optional) authentication.getPrincipal();
            if (!token.isPresent()) throw new BadCredentialsException("Invalid token");

            Authentication tokenAuthentication = getAuthentication((String) token.get());
            if(tokenAuthentication == null) throw new BadCredentialsException("Invalid token or token expired");
            return tokenAuthentication;
        }

        public Authentication getAuthentication(String token) throws AppworkException {
            try {
                Account account = tokenService.get(token);
                if(account == null) throw new AppworkException("Invalid User", ResponseCode.INVALID_AUTH);
                AuthenticationFilter.AuthenticationWithToken resultOfAuthentication = new AuthenticationFilter.AuthenticationWithToken(account.getUsername(), null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
                resultOfAuthentication.setToken(token);
                return resultOfAuthentication;
            }catch (Exception e){
                throw new AppworkException(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR);
            }
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(PreAuthenticatedAuthenticationToken.class);
        }
    }

}
