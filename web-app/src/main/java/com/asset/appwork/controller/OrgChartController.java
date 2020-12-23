package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.otds.Otds;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.GroupRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/org")
@RestController
public class OrgChartController {

    @Autowired
    TokenService tokenService;
    @Autowired
    Environment env;

    @Autowired
    GroupRepository groupRepository;

    @PostMapping("/unit/create")
    public ResponseEntity<AppResponse<JsonNode>> createUnit(@RequestHeader("X-Auth-Token") String token,
                                                          @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.create(props)));
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

    @GetMapping("/unit/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readUnit(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("id")Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readById(id)));
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

    @PutMapping("/unit/update/{id}")
    public String updateUnit(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("id")Long id,
                                                          @RequestBody String props
    ) {
        String response = "";
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                response = (entity.update(id, props) == null)? "Success": "Failed";
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            response = "Failed";
        }
        return response;
    }

    @DeleteMapping("/unit/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteUnit(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("id")Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.delete(id)));
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

    @PostMapping("/unit/read/list/{listName}")
    public ResponseEntity<AppResponse<JsonNode>> readUnitList(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("listName")String listName
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readList(listName)));
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

    @PostMapping("/unit/{unitId}/position/create")
    public ResponseEntity<AppResponse<JsonNode>> createPosition(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable("unitId")Long unitId,
                                                                @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.createChild(unitId, "Position", props)));
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

    @GetMapping("/unit/{unitId}/position/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readPosition(@RequestHeader("X-Auth-Token") String token,
                                                              @PathVariable("unitId")Long unitId,
                                                              @PathVariable("id")Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readChildById(unitId, "Position", id)));
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

    @PutMapping("/unit/{unitId}/position/update/{id}")
    public String updatePosition(@RequestHeader("X-Auth-Token") String token,
                                 @PathVariable("unitId")Long unitId,
                                 @PathVariable("id")Long id,
                                 @RequestBody String props
    ) {
        String response = "";
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                response = (entity.updateChild(unitId, "Position", id, props) == null)? "Success": "Failed";
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            response = "Failed";
        }
        return response;
    }

    @DeleteMapping("/unit/{unitId}/position/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deletePosition(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable("unitId")Long unitId,
                                                                @PathVariable("id")Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.deleteChild(unitId, "Position", id)));
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

    @GetMapping("/unit/position/read/list/{listName}")
    public ResponseEntity<AppResponse<JsonNode>> readPositionList(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("listName")String listName
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readChildList("Position", listName)));
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

    @PostMapping("/unit/{unitId}/position/items")
    public ResponseEntity<AppResponse<JsonNode>> readPositionItems(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("unitId")Long unitId
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readChildItems(unitId, "Position")));
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

    @PostMapping("/group/create")
    public ResponseEntity<AppResponse<JsonNode>> createGroup(@RequestHeader("X-Auth-Token") String token,
                                                             @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
                respBuilder.data(SystemUtil.convertStringToJsonNode(otds.createGroup(props)));
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


    @GetMapping("/group/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readGroup(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("id")Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readById(id)));
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

    @GetMapping("/group/findByCode/{code}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByCode(@RequestHeader("X-Auth-Token") String token,
                                                                 @PathVariable("code")String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                respBuilder.data(SystemUtil.convertStringToJsonNode(groupRepository.findByName(code).toString()));
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

    @GetMapping("/group/findByCodes/{codes}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByCodes(@RequestHeader("X-Auth-Token") String token,
                                                                 @PathVariable("codes")String codes
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                respBuilder.data(SystemUtil.convertStringToJsonNode(groupRepository.findByNameIn(Arrays.asList(codes.split(","))).toString()));
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

    @PutMapping("/group/update/{id}")
    public String updateGroup(@RequestHeader("X-Auth-Token") String token,
                             @PathVariable("id")Long id,
                             @RequestBody String props
    ) {
        String response = "";
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
                response = (entity.update(id, props) == null)? "Success": "Failed";
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            response = "Failed";
        }
        return response;
    }

    @DeleteMapping("/group/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteGroup(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("id")Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.delete(id)));
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

    @PostMapping("/group/read/list/{listName}")
    public ResponseEntity<AppResponse<JsonNode>> readGroupList(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("listName")String listName
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account != null) {
                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readList(listName)));
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

//    @PutMapping("/role/update/{roleName}")
//    public ResponseEntity<AppResponse<JsonNode>> updateRole(@RequestHeader("X-Auth-Token") String token,
//                                                            @PathVariable("roleName")String roleName,
//                                                            @RequestBody String props
//    ) {
//        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            if (account != null) {
//                Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(otds.updateRoleByRoleName(roleName, props)));
//            }
//        } catch (AppworkException | UnsupportedEncodingException | JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return respBuilder.build().getResponseEntity();
//    }
//
//    @DeleteMapping("/role/delete/{roleName}")
//    public ResponseEntity<AppResponse<JsonNode>> deleteRole(@RequestHeader("X-Auth-Token") String token,
//                                                                 @PathVariable("roleName") String roleName
//    ) {
//        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            if (account != null) {
//                Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(otds.deleteRoleByRoleName(roleName)));
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
//        } catch (AppworkException e) {
//            e.printStackTrace();
//            respBuilder.status(e.getCode());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return respBuilder.build().getResponseEntity();
//    }
//
//    @GetMapping("/role/read/all")
//    public ResponseEntity<AppResponse<JsonNode>> readAllRoles(@RequestHeader("X-Auth-Token") String token
//    ) {
//        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            if (account != null) {
//                Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(otds.getAllRoles()));
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
//        } catch (AppworkException e) {
//            e.printStackTrace();
//            respBuilder.status(e.getCode());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return respBuilder.build().getResponseEntity();
//    }
}
