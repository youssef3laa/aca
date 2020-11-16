package com.asset.appwork.controller;

import com.asset.appwork.config.AuthenticationFilter;
import com.asset.appwork.config.TokenService;
import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.IdentityComponentsPerson;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.IdentityComponentsPersonRepository;
import com.asset.appwork.response.AppResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Optionals;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by karim on 10/28/20.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user")
@RestController
public class UserController {


    @Autowired
    TokenService tokenService;
    @Autowired
    CordysManagement cordysManagement;


    @Bean
    private AuthenticationManager authenticationManager() {
        return new AuthenticationManager() {
            @SneakyThrows
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                Optional username = Optional.ofNullable( authentication.getPrincipal());
                Optional password = Optional.ofNullable( authentication.getCredentials());

                if (!username.isPresent() || !password.isPresent()) throw new BadCredentialsException("Invalid Domain User Credentials");

                CordysManagement.User user = cordysManagement.getUser((String)username.get(), (String)password.get(), "mod");

                Account account = new Account((String)username.get(),(String)password.get(),"mod",user.getSAMLart());

                AuthenticationFilter.AuthenticationWithToken resultOfAuthentication = new AuthenticationFilter.AuthenticationWithToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));

                String newToken = tokenService.generateNewToken(account);
                resultOfAuthentication.setToken(newToken);

                return resultOfAuthentication;

            }


        };
    }


    @PostMapping("/login")
    public ResponseEntity<AppResponse<String>> login(@RequestBody Account account) {

        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();

        try {
            CordysManagement.User user= cordysManagement.getUser(account.getUsername(), account.getPassword(), account.getOrganization());
            account.setSAMLart(user.getSAMLart());

            UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
            Authentication responseAuthentication = authenticationManager().authenticate(requestAuthentication);
            if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
                throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
            }

            SecurityContextHolder.getContext().setAuthentication(responseAuthentication);


            respBuilder.data((String) responseAuthentication.getDetails());

        } catch (JsonProcessingException e) {
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            respBuilder.status(e.getCode());
        }


        return respBuilder.build().getResponseEntity();
    }

    @Autowired
    IdentityComponentsPersonRepository identityComponentsPersonRepository;

    @GetMapping("/customers")
    public List<IdentityComponentsPerson> getAll() {

        Iterable<IdentityComponentsPerson> customers = identityComponentsPersonRepository.findAll();
        List<IdentityComponentsPerson> result = new ArrayList<>();
        customers.forEach(result::add);

        return result;
    }
}
