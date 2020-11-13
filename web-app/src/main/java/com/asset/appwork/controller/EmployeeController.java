package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.CordysUtil;
import com.asset.appwork.util.SystemUtil;
import com.asset.appwork.webservice.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by omar.sabry on 11/11/2020.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/employee")
@RestController
public class EmployeeController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysUtil cordysUtil;

    @PostMapping("/initiate")
    public ResponseEntity<AppResponse<String>> initiateEmployee(@RequestHeader("X-Auth-Token") String token,@RequestBody() String employeeJson){
        Employee employee = new Employee();
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
//        ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonNode = mapper.readTree(employeeJson);
//            jsonNode.get("Fname").asText();
//            jsonNode.get("Lname").asText();
//            jsonNode.get("Email").asText();
            String Fname = SystemUtil.readJSONField(employeeJson, "Fname");
            String Lname = SystemUtil.readJSONField(employeeJson, "Lname");
            String Email = SystemUtil.readJSONField(employeeJson, "Email");
            Account account = tokenService.readTokenData(token);
            if(account != null){
                String response = cordysUtil.sendRequest(account, employee.initiateEmployeeApproval(account.getSAMLart(),Fname,Lname,Email));
                respBuilder.data(response);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }
}
