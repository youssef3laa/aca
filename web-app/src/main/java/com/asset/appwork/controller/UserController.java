package com.asset.appwork.controller;

import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.IdentityComponentsPerson;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.IdentityComponentsPersonRepository;
import com.asset.appwork.response.AppResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.http.ResponseEntity;
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
    CordysManagement cordysManagement;

    @PostMapping("/login")
    public ResponseEntity<AppResponse<Account>> login(@RequestBody Account account) {
        AppResponse.ResponseBuilder<Account> respBuilder = AppResponse.builder();

        try {
            CordysManagement.User user= cordysManagement.getUser(account.getUsername(), account.getPassword(), account.getOrganization());
            account.setSAMLart(user.getSAMLart());
            respBuilder.data(account);
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
