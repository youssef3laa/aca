package com.asset.appwork.controller;

import com.asset.appwork.model.IdentityComponentsPerson;
import com.asset.appwork.repository.IdentityComponentsPersonRepository;
import com.asset.appwork.response.AppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Created by karim on 10/19/20.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class IdentityComponentsPersonController {

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
