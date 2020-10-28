package com.asset.appwork.controller;

import com.asset.appwork.cordys.CordysManagement;
import com.asset.appwork.model.User;
import com.asset.appwork.response.AppResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AppResponse<User>> login(@RequestParam String username, @RequestParam String password, @RequestParam String organization) {
        AppResponse.ResponseBuilder<User> respBuilder = AppResponse.builder();

        try {
            CordysManagement.User user= cordysManagement.create(username, password, organization);
            respBuilder.data(user.getUser());

            Optionals.ifPresentOrElse(Optional.of(respBuilder), (s)->{
                Object object = s;

                System.out.println(s);
            }, ()-> System.out.print(""));



        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return respBuilder.build().getResponseEntity();
    }

}
