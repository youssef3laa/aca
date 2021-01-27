package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.OrgChartService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/org")
@RestController
@Slf4j
public class OrgChartController {

    @Autowired
    TokenService tokenService;

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
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.createUnit(account, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/unit/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readUnit(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUnit(id).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/unit/update/{id}")
    public ResponseEntity<AppResponse<JsonNode>> updateUnit(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("id") Long id,
                                                            @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.updateUnit(account, id, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @DeleteMapping("/unit/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteUnit(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.deleteUnit(id);
            respBuilder.info("infoMessage", "Deleted Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/unit/read/list")
    public ResponseEntity<AppResponse<JsonNode>> readUnitList(@RequestHeader("X-Auth-Token") String token,
                                                              @RequestParam(value = "page") Optional<Integer> page,
                                                              @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Unit> unitPage = orgChartService.getAllUnits(page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(unitPage.getContent().toString()));
                    respBuilder.info("totalCount", unitPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getAllUnits().toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/unit/{id}/subUnit/{subUnitId}")
    public ResponseEntity<AppResponse<JsonNode>> addSubUnitToUnit(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("id") Long id,
                                                                  @PathVariable("subUnitId") Long subUnitId
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.addSubUnitToUnit(account, id, subUnitId);
            respBuilder.info("infoMessage", "Sub Unit added Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PutMapping("/unit/{code}/subUnitWithCode/{subUnitCode}")
    public ResponseEntity<AppResponse<JsonNode>> addSubUnitToUnitWithCodes(@RequestHeader("X-Auth-Token") String token,
                                                                           @PathVariable("code") String code,
                                                                           @PathVariable("subUnitCode") String subUnitCode
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.addSubUnitToUnit(account, code, subUnitCode);
            respBuilder.info("infoMessage", "Sub Unit added Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/unit/{code}/down/all/unitTypeCode/{unitTypeCode}")
    public ResponseEntity<AppResponse<JsonNode>> readUnitChildrenRecursivelyFilteredByUnitTypeCode(@RequestHeader("X-Auth-Token") String token,
                                                                                                   @PathVariable("code") String code,
                                                                                                   @PathVariable("unitTypeCode") String unitTypeCode,
                                                                                                   @RequestParam(value = "page") Optional<Integer> page,
                                                                                                   @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Unit> unitPage = orgChartService.getUnitChildrenRecursivelyFilteredByUnitTypeCode(code, unitTypeCode, page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(unitPage.getContent().toString()));
                    respBuilder.info("totalCount", unitPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUnitChildrenRecursivelyFilteredByUnitTypeCode(code, unitTypeCode).toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.createPosition(account, unitId, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/unit/position/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readPosition(@RequestHeader("X-Auth-Token") String token,
                                                              @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getPosition(id).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.updatePosition(account, unitId, id, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @DeleteMapping("/unit/position/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deletePosition(@RequestHeader("X-Auth-Token") String token,
                                                                @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.deletePosition(id);
            respBuilder.info("infoMessage", "Deleted Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
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
    public ResponseEntity<AppResponse<JsonNode>> readPositionList(@RequestHeader("X-Auth-Token") String token,
                                                                  @RequestParam(value = "page") Optional<Integer> page,
                                                                  @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Position> positionPage = orgChartService.getAllPositions(page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(positionPage.getContent().toString()));
                    respBuilder.info("totalCount", positionPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getAllPositions().toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
                                                                       @PathVariable("code") String code,
                                                                       @RequestParam(value = "page") Optional<Integer> page,
                                                                       @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Unit> unitPage = orgChartService.getUnitChildren(code, page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(unitPage.getContent().toString()));
                    respBuilder.info("totalCount", unitPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUnitChildren(code).toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUnitParent(code).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PostMapping("/group/create")
    public ResponseEntity<AppResponse<JsonNode>> createGroup(@RequestHeader("X-Auth-Token") String token,
                                                             @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.createGroup(account, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> readGroup(@RequestHeader("X-Auth-Token") String token,
                                                           @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroup(id).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/findByCode/{code}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByCode(@RequestHeader("X-Auth-Token") String token,
                                                                 @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupByName((code)).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/findByCodes/{codes}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByCodes(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("codes") String codes,
                                                                  @RequestParam(value = "page") Optional<Integer> page,
                                                                  @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Group> groupPage = orgChartService.getGroupsByNames(codes, page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(groupPage.getContent().toString()));
                    respBuilder.info("totalCount", groupPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupsByNames(codes).toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/findByUnitCodes/{codes}")
    public ResponseEntity<AppResponse<JsonNode>> findGroupByUnitCodes(@RequestHeader("X-Auth-Token") String token,
                                                                      @PathVariable("codes") String codes,
                                                                      @RequestParam(value = "page") Optional<Integer> page,
                                                                      @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Group> groupPage = orgChartService.getGroupsByUnitNames(codes, page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(groupPage.getContent().toString()));
                    respBuilder.info("totalCount", groupPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupsByUnitNames(codes).toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/{code}/down")
    public ResponseEntity<AppResponse<JsonNode>> getGroupUnitChildrenByCode(@RequestHeader("X-Auth-Token") String token,
                                                                            @PathVariable("code") String code,
                                                                            @RequestParam(value = "page") Optional<Integer> page,
                                                                            @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Group> groupPage = orgChartService.getGroupChildren(code, page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(groupPage.getContent().toString()));
                    respBuilder.info("totalCount", groupPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupChildren(code).toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/{code}/up")
    public ResponseEntity<AppResponse<JsonNode>> getGroupUnitParentsByCode(@RequestHeader("X-Auth-Token") String
                                                                                   token,
                                                                           @PathVariable("code") String code
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupParent(code).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/{code}/down/all/unitTypeCode/{unitTypeCode}")
    public ResponseEntity<AppResponse<JsonNode>> getGroupChildrenRecursivelyFilteredByUnitTypeCode(@RequestHeader("X-Auth-Token") String token,
                                                                                                   @PathVariable("code") String code,
                                                                                                   @PathVariable("unitTypeCode") String unitTypeCode,
                                                                                                   @RequestParam(value = "page") Optional<Integer> page,
                                                                                                   @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Group> groupPage = orgChartService.getGroupChildrenRecursivelyFilteredByUnitTypeCode(code, unitTypeCode, page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(groupPage.getContent().toString()));
                    respBuilder.info("totalCount", groupPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupChildrenRecursivelyFilteredByUnitTypeCode(code, unitTypeCode).toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/up")
    public ResponseEntity<AppResponse<JsonNode>> getGroupParentsOfLoggedInUser(@RequestHeader("X-Auth-Token") String
                                                                                       token
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupParentsOfLoggedInUser(account).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/group/all/unitTypeCodes/{codes}")
    public ResponseEntity<AppResponse<JsonNode>> getGroupsByUnitTypeCodes(@RequestHeader("X-Auth-Token") String token,
                                                                          @PathVariable("codes") String codes,
                                                                          @RequestParam(value = "page") Optional<Integer> page,
                                                                          @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Group> groupPage = orgChartService.getGroupsByUnitTypeCodes(codes, page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(groupPage.getContent().toString()));
                    respBuilder.info("totalCount", groupPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getGroupsByUnitTypeCodes(codes).toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/group/update/{id}")
    public ResponseEntity<AppResponse<JsonNode>> updateGroup(@RequestHeader("X-Auth-Token") String token,
                                                             @PathVariable("id") Long id,
                                                             @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.updateGroup(account, id, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/group/{groupId}/relation/unit/add")
    public ResponseEntity<AppResponse<JsonNode>> updateGroupUnitRelation(@RequestHeader("X-Auth-Token") String token,
                                                                         @PathVariable("groupId") Long groupId,
                                                                         @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.updateGroupUnitRelation(account, groupId, props);
            respBuilder.info("infoMessage", "Relation added Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @PutMapping("/group/{groupCode}/relation/unit/add/{unitCode}")
    public ResponseEntity<AppResponse<JsonNode>> updateGroupUnitRelationByCodes(@RequestHeader("X-Auth-Token") String token,
                                                                                @PathVariable("groupCode") String groupCode,
                                                                                @PathVariable("unitCode") String unitCode
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.updateGroupUnitRelationByCodes(account, groupCode, unitCode);
            respBuilder.info("infoMessage", "Relation added Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
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
            orgChartService.deleteGroup(account, id);
            respBuilder.info("infoMessage", "Deleted Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/group/read/list")
    public ResponseEntity<AppResponse<JsonNode>> readGroupList(@RequestHeader("X-Auth-Token") String token,
                                                               @RequestParam(value = "page") Optional<Integer> page,
                                                               @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<Group> groupPage = orgChartService.getAllGroups(page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(groupPage.getContent().toString()));
                    respBuilder.info("totalCount", groupPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getAllGroups().toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PostMapping("/user/create")
    public ResponseEntity<AppResponse<JsonNode>> createUser(@RequestHeader("X-Auth-Token") String token,
                                                            @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.createUser(account, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/user/{id}")
    public ResponseEntity<AppResponse<JsonNode>> getUserById(@RequestHeader("X-Auth-Token") String
                                                                     token,
                                                             @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUser(id).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @GetMapping("/user/userId/{userId}")
    public ResponseEntity<AppResponse<JsonNode>> getUserByUserId(@RequestHeader("X-Auth-Token") String
                                                                         token,
                                                                 @PathVariable("userId") String userId
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getUserByUserId(userId).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/user/update/{id}")
    public ResponseEntity<AppResponse<JsonNode>> updateUser(@RequestHeader("X-Auth-Token") String
                                                                    token,
                                                            @PathVariable("id") Long id,
                                                            @RequestBody String props
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.updateUser(account, id, props).toString()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteUser(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("id") Long id
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            orgChartService.deleteUser(account, id);
            respBuilder.info("infoMessage", "Deleted Successfully");
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/user/read/list")
    public ResponseEntity<AppResponse<JsonNode>> getAllUsers(@RequestHeader("X-Auth-Token") String token,
                                                             @RequestParam(value = "page") Optional<Integer> page,
                                                             @RequestParam(value = "size") Optional<Integer> size
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                if (page.isPresent() && size.isPresent()) {
                    Page<User> userPage = orgChartService.getAllUsers(page.get(), size.get());
                    respBuilder.data(SystemUtil.convertStringToJsonNode(userPage.getContent().toString()));
                    respBuilder.info("totalCount", userPage.getTotalElements());
                } else {
                    respBuilder.data(SystemUtil.convertStringToJsonNode(orgChartService.getAllUsers().toString()));
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/user/{userId}/assign/group/{groupCode}")
    public ResponseEntity<AppResponse<JsonNode>> assignUserToGroup(@RequestHeader("X-Auth-Token") String token,
                                                                   @PathVariable("userId") String userId,
                                                                   @PathVariable("groupCode") String groupCode
    ) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) return respBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            try {
                orgChartService.assignUserToGroup(account, userId, groupCode);
                respBuilder.info("infoMessage", "User added to group " + groupCode + " Successfully");
                respBuilder.status(ResponseCode.SUCCESS);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                respBuilder.info("errorMessage", e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            respBuilder.info("errorMessage", e.getMessage());
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }
}
