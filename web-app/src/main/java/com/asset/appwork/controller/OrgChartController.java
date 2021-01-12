package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.asset.appwork.otds.Otds;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.GroupRepository;
import com.asset.appwork.repository.PositionRepository;
import com.asset.appwork.repository.UnitRepository;
import com.asset.appwork.repository.UserRepository;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/org")
@RestController
@Slf4j
public class OrgChartController {

    @Autowired
    TokenService tokenService;
    @Autowired
    Environment env;

    @Autowired
    UnitRepository unitRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PositionRepository positionRepository;

    @Autowired
    OrgChartService orgChartService;

    @Transactional
    @PostMapping("/unit/create")
    public ResponseEntity<AppResponse<JsonNode>> createUnit(@RequestHeader("X-Auth-Token") String token,
                                                            @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            log.info("Create Unit by: " + account.getUsername());
            orgChartService.createUnit(account, props).ifPresentOrElse(unit -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(unit.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, () -> {
                log.error(ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.info("errorMessage", ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.status(ResponseCode.READ_ENTITY_FAILURE);
            });
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/unit/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readUnit(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.getUnit(id).ifPresentOrElse(unit -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(unit.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, () -> {
                try {
                    throw new AppworkException(ResponseCode.READ_ENTITY_FAILURE);
                } catch (AppworkException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                    respBuilder.info("errorMessage", e.getMessage());
                    respBuilder.status(e.getCode());
                }
            });
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PutMapping("/unit/update/{id}")
    public ResponseEntity<AppResponse<JsonNode>> updateUnit(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("id") Long id,
                                                            @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.updateUnit(account, id, props).ifPresentOrElse(unit -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(unit.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, () -> {
                log.error(ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.info("errorMessage", ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.status(ResponseCode.READ_ENTITY_FAILURE);
            });
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @DeleteMapping("/unit/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteUnit(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.deleteUnit(id); // TODO: Transactional Roll back results in the error message not being displayed
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PostMapping("/unit/read/list")
    public ResponseEntity<AppResponse<JsonNode>> readUnitList(@RequestHeader("X-Auth-Token") String token
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getAllUnits().toString()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PostMapping("/unit/{unitId}/position/create")
    public ResponseEntity<AppResponse<JsonNode>> createPosition(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable("unitId") Long unitId,
                                                                @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.createPosition(account, unitId, props).ifPresentOrElse(position -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(position.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, () -> {
                log.error(ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.info("errorMessage", ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.status(ResponseCode.READ_ENTITY_FAILURE);
            });
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/unit/position/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readPosition(@RequestHeader("X-Auth-Token") String token,
                                                              @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.getPosition(id).ifPresentOrElse(position -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(position.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, () -> {
                try {
                    throw new AppworkException(ResponseCode.READ_ENTITY_FAILURE);
                } catch (AppworkException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                    respBuilder.info("errorMessage", e.getMessage());
                    respBuilder.status(e.getCode());
                }
            });
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PutMapping("/unit/{unitId}/position/update/{id}")
    public ResponseEntity<AppResponse<JsonNode>> updatePosition(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable("unitId") Long unitId,
                                                                @PathVariable("id") Long id,
                                                                @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.updatePosition(account, unitId, id, props).ifPresentOrElse(position -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(position.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, () -> {
                log.error(ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.info("errorMessage", ResponseCode.READ_ENTITY_FAILURE.getMessage());
                respBuilder.status(ResponseCode.READ_ENTITY_FAILURE);
            });
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @DeleteMapping("/unit/position/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deletePosition(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.deletePosition(id);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/unit/position/read/list")
    public ResponseEntity<AppResponse<JsonNode>> readPositionList(@RequestHeader("X-Auth-Token") String token
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getAllPositions().toString()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/unit/{code}/down")
    public ResponseEntity<AppResponse<JsonNode>> getUnitChildrenByCode(@RequestHeader("X-Auth-Token") String token,
                                                                       @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUnitChildren(code).toString()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/unit/{code}/up")
    public ResponseEntity<AppResponse<JsonNode>> getUnitParentsByCode(@RequestHeader("X-Auth-Token") String token,
                                                                      @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.getUnitParent(code).ifPresentOrElse((unit -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(unit.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }), () -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PostMapping("/group/create")
    public ResponseEntity<AppResponse<JsonNode>> createGroup(@RequestHeader("X-Auth-Token") String token,
                                                             @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
            respBuilder.data(SystemUtil.convertStringToJsonNode(otds.createGroup(props)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readGroup(@RequestHeader("X-Auth-Token") String token,
                                                           @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
            respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readById(id)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/findByCode/{code}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByCode(@RequestHeader("X-Auth-Token") String token,
                                                                 @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            groupRepository.findByNameAndGroupCodeNotNull(code).ifPresentOrElse((group -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(group.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }), () -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/findByCodes/{codes}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByCodes(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("codes") String codes
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(groupRepository.findByNameInAndGroupCodeNotNull(Arrays.asList(codes.trim().split("\\s*,\\s*"))).toString()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/findByUnitCode/{code}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByUnitCode(@RequestHeader("X-Auth-Token") String token,
                                                                     @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            unitRepository.findByNameAndUnitCodeNotNull(code).ifPresentOrElse((unit -> groupRepository.findByUnit(unit).ifPresentOrElse((groups -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(groups.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }), () -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            })), () -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/findByUnitCodes/{codes}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByUnitCodes(@RequestHeader("X-Auth-Token") String token,
                                                                      @PathVariable("codes") String codes
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(groupRepository.findByUnitIn(new HashSet<>(unitRepository.findByNameInAndUnitCodeNotNull(Arrays.asList(codes.trim().split("\\s*,\\s*"))))).toString()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/{code}/down")
    public ResponseEntity<AppResponse<JsonNode>> getGroupUnitChildrenByCode(@RequestHeader("X-Auth-Token") String token,
                                                                            @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            groupRepository.findByNameAndGroupCodeNotNull(code).ifPresentOrElse(
                    group -> {
                        try {
                            respBuilder.data(SystemUtil.convertStringToJsonNode(groupRepository.findByUnitIn(new HashSet<>(unitRepository.findByParent(group.getUnit()))).toString()));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }, () -> {
                        try {
                            respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/{code}/up")
    public ResponseEntity<AppResponse<JsonNode>> getGroupUnitParentsByCode(@RequestHeader("X-Auth-Token") String
                                                                                   token,
                                                                           @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            groupRepository.findByNameAndGroupCodeNotNull(code).ifPresentOrElse(
                    group -> unitRepository.findByGroup(group).ifPresentOrElse(
                            unit -> unitRepository.findByChild(unit).ifPresentOrElse(
                                    parentUnit -> groupRepository.findByUnit(parentUnit).ifPresentOrElse(parentGroup -> {
                                        try {
                                            respBuilder.data(SystemUtil.convertStringToJsonNode(parentGroup.toString()));
                                        } catch (JsonProcessingException e) {
                                            e.printStackTrace();
                                        }
                                    }, () -> {
                                        try {
                                            respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                                        } catch (JsonProcessingException e) {
                                            e.printStackTrace();
                                        }
                                    }), () -> {
                                        try {
                                            respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                                        } catch (JsonProcessingException e) {
                                            e.printStackTrace();
                                        }
                                    }), () -> {
                                try {
                                    respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }), () -> {
                        try {
                            respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/{code}/down/all/unitTypeCode/{unitTypeCode}")
    public ResponseEntity<AppResponse<JsonNode>> getGroupChildrenRecursivelyFilteredByUnitTypeCode(@RequestHeader("X-Auth-Token") String token,
                                                                                                   @PathVariable("code") String code,
                                                                                                   @PathVariable("unitTypeCode") String unitTypeCode
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupChildrenRecursivelyFilteredByUnitTypeCode(code, unitTypeCode).toString()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PutMapping("/group/update/{id}")
    public ResponseEntity<AppResponse<JsonNode>> updateGroup(@RequestHeader("X-Auth-Token") String token,
                                                             @PathVariable("id") Long id,
                                                             @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
            respBuilder.data(SystemUtil.convertStringToJsonNode(entity.update(id, props)));
        } catch (AppworkException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @DeleteMapping("/group/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteGroup(@RequestHeader("X-Auth-Token") String token,
                                                             @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
            respBuilder.data(SystemUtil.convertStringToJsonNode(entity.delete(id)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PostMapping("/group/read/list/{listName}")
    public ResponseEntity<AppResponse<JsonNode>> readGroupList(@RequestHeader("X-Auth-Token") String token,
                                                               @PathVariable("listName") String listName
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
            respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readList(listName)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/user/{userId}")
    public ResponseEntity<AppResponse<JsonNode>> getUserByUserId(@RequestHeader("X-Auth-Token") String
                                                                         token,
                                                                 @PathVariable("userId") String userId
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            userRepository.findByUserId(userId).ifPresentOrElse((user) -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(user.toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, () -> {
                try {
                    respBuilder.data(SystemUtil.convertStringToJsonNode("{}"));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    //    @PostMapping("/unit/{unitId}/position/items")
//    public ResponseEntity<AppResponse<JsonNode>> readPositionItems(@RequestHeader("X-Auth-Token") String token,
//                                                                  @PathVariable("unitId")Long unitId
//    ) {
//        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
//                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
//                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readChildItems(unitId, "Position")));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
//        } catch (AppworkException e) {
//            e.printStackTrace();
//            respBuilder.status(e.getCode());
//        }
//        return respBuilder.build().getResponseEntity();
//    }
//
//
//    @GetMapping("/unit/{unitId}/group/read/{id}")
//    public ResponseEntity<AppResponse<JsonNode>> readGroup(@RequestHeader("X-Auth-Token") String token,
//                                                              @PathVariable("unitId")Long unitId,
//                                                              @PathVariable("id")Long id
//    ) {
//        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
//                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
//                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.readChildById(unitId, "Position", id)));
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
//        } catch (AppworkException e) {
//            e.printStackTrace();
//            respBuilder.status(e.getCode());
//        }
//        return respBuilder.build().getResponseEntity();
//    }
//

    @Transactional
    @PutMapping("/group/{groupId}/relation/unit/add")
    public ResponseEntity<AppResponse<JsonNode>> updateGroupUnitRelation(@RequestHeader("X-Auth-Token") String token,
                                                                         @PathVariable("groupId") Long groupId,
                                                                         @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
            respBuilder.data(SystemUtil.convertStringToJsonNode(entity.addRelation(groupId, "Unit", props)));
        } catch (AppworkException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return respBuilder.build().getResponseEntity();
    }
//
//    @DeleteMapping("/unit/{unitId}/position/delete/{id}")
//    public ResponseEntity<AppResponse<JsonNode>> deleteGroup(@RequestHeader("X-Auth-Token") String token,
//                                                                @PathVariable("unitId")Long unitId,
//                                                                @PathVariable("id")Long id
//    ) {
//        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
//                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "OrganizationalUnit");
//                respBuilder.data(SystemUtil.convertStringToJsonNode(entity.deleteChild(unitId, "Position", id)));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
//        } catch (AppworkException e) {
//            e.printStackTrace();
//            respBuilder.status(e.getCode());
//        }
//        return respBuilder.build().getResponseEntity();
//    }

//    @PutMapping("/group/{groupId}/relation/position//add/{id}")
//    public String addPositionRelationToGroup(@RequestHeader("X-Auth-Token") String token,
//                                             @PathVariable("groupId")Long groupId,
//                                             @PathVariable("id")Long id
//    ) {
//        String response = "";
//        try {
//            Account account = tokenService.get(token);
//            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
//                Entity entity = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group");
//                response = (entity.addToOneRelation(groupId, "Position", new Platform.IdentityTargetId(id)) == null)? "Success": "Failed";
//        } catch (AppworkException | JsonProcessingException e) {
//            e.printStackTrace();
//            response = "Failed";
//        }
//        return response;
//    }

//    @PutMapping("/role/update/{roleName}")
//    public ResponseEntity<AppResponse<JsonNode>> updateRole(@RequestHeader("X-Auth-Token") String token,
//                                                            @PathVariable("roleName")String roleName,
//                                                            @RequestBody String props
//    ) {
//        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
//        try {
//            Account account = tokenService.get(token);
//            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
//                Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(otds.updateRoleByRoleName(roleName, props)));
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
//            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
//                Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(otds.deleteRoleByRoleName(roleName)));
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
//            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
//                Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
//                respBuilder.data(SystemUtil.convertStringToJsonNode(otds.getAllRoles()));
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

//    public Optional<Group> createGroup(Account account, String props) {
//        Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
//        otds.createGroup(props);
//    }
}
