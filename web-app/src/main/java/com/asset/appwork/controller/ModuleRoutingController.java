package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.GroupType;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import com.asset.appwork.orgchart.ModuleRouting;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.schema.OutputSchema;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/module/routing")

@Slf4j
@RestController
public class ModuleRoutingController {
    @Autowired
    TokenService tokenService;
    @Autowired
    ModuleRouting moduleRouting;
    @Autowired
    OrgChartService orgChartService;

    @Transactional
    @PostMapping("/test")
    public ResponseEntity<AppResponse<String>> complete(@RequestHeader("X-Auth-Token") String token, @RequestBody OutputSchema outputSchema) {
        AppResponse.ResponseBuilder<String> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);

            String response = moduleRouting.calculateOutputSchema(outputSchema, account);
            respBuilder.data(response);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/get/related")
    public ResponseEntity<AppResponse<JsonNode>> getRelatedUsers(@RequestHeader("X-Auth-Token") String token,
                                                                 @RequestParam String unitTypeCode,
                                                                 @RequestParam String returnType,
                                                                 @RequestParam Long requestId,
                                                                 @RequestParam(required = false, defaultValue = "") String input,
                                                                 @RequestParam(required = false, defaultValue = "false") Boolean sameLevel) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            JsonNode nodes;
            Account account = tokenService.get(token);
            User loggedInUser = orgChartService.getLoggedInUser(account);
            Optional<Group> userGroup = loggedInUser.getGroup().stream().findFirst();
            if(userGroup.isPresent()){
                Unit userUnit = userGroup.get().getUnit();
                String direction = moduleRouting.getDirection(requestId,userUnit.getUnitTypeCode(),unitTypeCode);
                //Case units
                if(returnType.equals("units")){
                    if(input.isEmpty()) input = userUnit.getUnitCode();
                    if(direction.equals("same")){
                        if(sameLevel){
                            List<Unit> units = new ArrayList<>();
                            units.add(userUnit);
                            nodes = SystemUtil.convertStringToJsonNode(units.toString());
                        }else{
                            nodes = SystemUtil.convertStringToJsonNode(orgChartService.getUnitChildrenRecursivelyFilteredByUnitTypeCode("COC", unitTypeCode).toString());
                        }
                    }else if(direction.equals("up")){
                        nodes = SystemUtil.convertStringToJsonNode(orgChartService.getUnitParentsRecursivelyFilteredByUnitTypeCode(input, unitTypeCode).toString());
                    }else{
                        nodes = SystemUtil.convertStringToJsonNode(orgChartService.getUnitChildrenRecursivelyFilteredByUnitTypeCode(input, unitTypeCode).toString());
                    }
                }

                // Case Roles
                else if(returnType.equals("roles")){
                    if(direction.equals("same")){
                        if(sameLevel){
                            List<Group> groups = new ArrayList<>();
                            groups.add(userGroup.get());
                            nodes = SystemUtil.convertStringToJsonNode(groups.toString());
                        }else{
                            if(input.isEmpty()) input = userUnit.getUnitCode();
                            nodes = SystemUtil.convertStringToJsonNode(orgChartService.getGroupsByUnitNames(input).toString());
                        }
                    }else if(direction.equals("up")){
                        if(input.isEmpty()) input = userUnit.getUnitCode();
                        List<Unit> parentUnit = orgChartService.getUnitParentsRecursivelyFilteredByUnitTypeCode(input,unitTypeCode);
                        nodes = SystemUtil.convertStringToJsonNode(orgChartService.getGroupsInUnitAndGroupType(parentUnit.get(0), GroupType.HEAD).toString());
                    }else{
                        if(input.isEmpty()) input = userGroup.get().getGroupCode();
                        nodes = SystemUtil.convertStringToJsonNode(orgChartService.getGroupChildrenRecursivelyFilteredByUnitTypeCode(input, unitTypeCode).toString());
                    }
                }

                //Case Users
                else{
                    if(direction.equals("same")){
                        if(sameLevel){
                            List<User> users = new ArrayList<>();
                            users.add(loggedInUser);
                            nodes = SystemUtil.convertStringToJsonNode(users.toString());
                        }else{
                            GroupType groupType = userGroup.get().getType();
                            List<User> users = orgChartService.getUsersInUnitAndGroupType(userUnit, groupType);
                            nodes = SystemUtil.convertStringToJsonNode(users.toString());
                        }
                    }else if(direction.equals("up")){
                            if(input.isEmpty()) input = userUnit.getUnitCode();
                            List<Unit> parentUnit = orgChartService.getUnitParentsRecursivelyFilteredByUnitTypeCode(input,unitTypeCode);
                            List<User> users = orgChartService.getUsersInUnitAndGroupType(parentUnit.get(0),GroupType.HEAD);
                            nodes = SystemUtil.convertStringToJsonNode(users.toString());
                    }else{
                        if(returnType.equals("user")){
                            if(input.isEmpty()) input = userGroup.get().getGroupCode();
                            Group group = orgChartService.getGroupByName(input);
                            nodes = SystemUtil.convertStringToJsonNode(orgChartService.getUsersInGroup(group).toString());
                        }else{
                            if(input.isEmpty()) input = userUnit.getUnitCode();
                            GroupType type= orgChartService.getGroupTypeByLevel(returnType);
                            Unit unit = orgChartService.getUnitByName(input);
                            List<User> users = orgChartService.getUsersInUnitAndGroupType(unit, type);
                            nodes = SystemUtil.convertStringToJsonNode(users.toString());
                        }
                    }
                }

                respBuilder.data(nodes);
            }else{
                throw new AppworkException(ResponseCode.USER_GROUP_FAILURE);
            }
        }catch (JsonProcessingException e) {
          e.printStackTrace();
          respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

}
