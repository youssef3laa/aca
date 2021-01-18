package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cs.AppworkCSOperations;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.AttachmentSort;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.AttachmentSortService;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by omaradl on 11/16/2020.
 */
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/document")
@RestController
public class DocumentsController {
    @Autowired
    TokenService tokenService;

    @Autowired
    AttachmentSortService attachmentSortService;

    @PostMapping("/upload")
    public ResponseEntity<AppResponse<JsonNode>> uploadFile(@RequestHeader("X-Auth-Token") String token,
                                                            @RequestParam("file") MultipartFile[] files,
                                                            @RequestParam("parentId") Long parentId) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonNodes = objectMapper.createArrayNode();
        String currentFileName = "";
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            try {
                for (MultipartFile mf : files) {
                    currentFileName = mf.getOriginalFilename();
                    Http http = appworkCSOperations.
                            uploadDocument(mf.getBytes(), parentId, mf.getOriginalFilename(), String.valueOf(144));
                    System.out.println(http.getResponse());
                    System.out.println(http.getStatusCode());
                    ObjectNode fileNode = objectMapper.createObjectNode();
                    fileNode.set(mf.getOriginalFilename(), objectMapper.readTree(http.getResponse()));
                    jsonNodes.add(fileNode);

                }
            } catch (IOException e) {
                log.error(e.getMessage());
                respBuilder.status(ResponseCode.UNSUPPORTED_FILE_TYPE);
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.set(currentFileName, objectMapper.readTree(e.getMessage()));
                jsonNodes.add(errorNode);
            }
        } catch (AppworkException | JsonProcessingException e) {
            log.error(e.getMessage());
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put(currentFileName, e.getMessage());
            jsonNodes.add(errorNode);

        }

        respBuilder.data(jsonNodes);
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/list/{parentId}")
    public ResponseEntity<AppResponse<JsonNode>> listSubNodes(@RequestHeader("X-Auth-Token") String token,
                                                              @PathVariable("parentId") Long parentId,
                                                              AppworkCSOperations.DocumentQuery documentQuery) {

        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.getSubNodes(parentId, documentQuery);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
            respBuilder.data(SystemUtil.convertStringToJsonNode(http.getResponse()));
        } catch (AppworkException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", e.getCode().getCode());
            respBuilder.data(obj);
            respBuilder.status(e.getCode());
        } catch (JsonProcessingException | IllegalAccessException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            respBuilder.data(obj);
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }

    @DeleteMapping("delete/{nodeId}")
    public ResponseEntity<AppResponse<JsonNode>> deleteNode(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("nodeId") Long nodeId) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.deleteNode(nodeId);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        } catch (AppworkException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", e.getCode().getCode());
            respBuilder.data(obj);
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }


    @GetMapping("sort")
    public ResponseEntity<AppResponse<JsonNode>> getSort(@RequestHeader("X-Auth-Token") String token,
                                                         @RequestParam("requestEntityId") String requestEntityId,
                                                         @RequestParam("bwsId") String bwsId) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            List<AttachmentSort> attachmentSortList = attachmentSortService.getAttachmentSort(requestEntityId, bwsId);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode jsonNodes = mapper.valueToTree(attachmentSortList);
            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(jsonNodes);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @PostMapping(value = "sort", consumes = "application/json")
    public ResponseEntity<AppResponse<JsonNode>> createSortAttachmentRecord(@RequestHeader("X-Auth-Token") String token,
                                                                            @RequestBody AttachmentSort attachmentSort) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AttachmentSort attachmentSortObj = attachmentSortService.createAttachmentSort(attachmentSort, account);
            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode jsonNodes = mapper.value(attachmentSortList);
            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(mapper.valueToTree(attachmentSortObj));
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @PostMapping(value = "sort/bulk", consumes = "application/json")
    public ResponseEntity<AppResponse<JsonNode>> createMultipleSortAttachmentRecord(@RequestHeader("X-Auth-Token") String token,
                                                                                    @RequestBody List<AttachmentSort> attachmentSortList) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            ObjectMapper mapper = new ObjectMapper();

            ArrayNode jsonNodes = mapper.createArrayNode();
            for (AttachmentSort attachmentSort : attachmentSortList) {
                AttachmentSort attachmentSortObj = attachmentSortService.createAttachmentSort(attachmentSort, account);
                jsonNodes.add(mapper.valueToTree(attachmentSortObj));
            }
            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(jsonNodes);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }


    @DeleteMapping("sort/{id}")
    public ResponseEntity<AppResponse<JsonNode>> deleteSortAttachmentRecord(@RequestHeader("X-Auth-Token") String token,
                                                                            @PathVariable("id") Long id) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);

            String response = attachmentSortService.deleteAttachmentSort(id, account);
            System.out.println("delete response");
            System.out.println(response);
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
            ObjectMapper objectMapper = new ObjectMapper();
            respBuilder.data(objectMapper.createObjectNode().put("errorMessage", e.getMessage()));

        }
        return respBuilder.build().getResponseEntity();


    }

    @DeleteMapping("sort/bulk/{ids}")
    public ResponseEntity<AppResponse<JsonNode>> deleteMultipleSortAttachmentRecord(@RequestHeader("X-Auth-Token") String token,
                                                                                    @PathVariable("ids") String ids) {
        String[] stringList = ids.split(",");
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);

            for (String id : stringList) {
                attachmentSortService.deleteAttachmentSort(Long.valueOf(id), account);
            }
            respBuilder.status(ResponseCode.SUCCESS);

        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
            try {
                respBuilder.data(SystemUtil.convertStringToJsonNode(e.getMessage()));
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        }
        return respBuilder.build().getResponseEntity();


    }

    @PostMapping("sort/update")
    public ResponseEntity<AppResponse<JsonNode>> updateSortAttachmentRecord(@RequestHeader("X-Auth-Token") String token,
                                                                            @RequestBody AttachmentSort attachmentSort) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        AttachmentSort resultantAttachmentSort;
        try {
            resultantAttachmentSort = attachmentSortService.updateAttachmentSort(attachmentSort);
            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(new ObjectMapper().valueToTree(resultantAttachmentSort));
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("sort/update/bulk")
    public ResponseEntity<AppResponse<JsonNode>> updateMultipleSortAttachmentRecord(@RequestHeader("X-Auth-Token") String token,
                                                                                    @RequestBody List<AttachmentSort> attachmentSortList) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        AttachmentSort resultantAttachmentSort;
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();
        try {
            for (AttachmentSort attachmentSort : attachmentSortList) {
                resultantAttachmentSort = attachmentSortService.updateAttachmentSort(attachmentSort);
                jsonArray.add(mapper.valueToTree(resultantAttachmentSort));
            }
            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(jsonArray);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }


    //versions management

    @SuppressWarnings("DuplicatedCode")
    @GetMapping("/version/list/{nodeId}")
    public ResponseEntity<AppResponse<JsonNode>> listNodeVersions(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("nodeId") Long nodeId,
                                                                  AppworkCSOperations.DocumentQuery documentQuery) {

        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.getNodeVersions(nodeId, documentQuery);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
            respBuilder.data(SystemUtil.convertStringToJsonNode(http.getResponse()));
        } catch (AppworkException | IllegalAccessException | JsonProcessingException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            respBuilder.data(obj);
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/version/{nodeId}/{versionId}")
    public ResponseEntity<AppResponse<JsonNode>> getSpecificNodeVersion(@RequestHeader("X-Auth-Token") String token,
                                                                        @PathVariable("nodeId") Long nodeId,
                                                                        @PathVariable("versionId") Integer versionId,
                                                                        AppworkCSOperations.DocumentQuery documentQuery) {

        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.getSpecifiedNodeVersion(nodeId, versionId, documentQuery);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
            respBuilder.data(SystemUtil.convertStringToJsonNode(http.getResponse()));
        } catch (AppworkException | IllegalAccessException | JsonProcessingException e) {
            e.printStackTrace();
            ObjectNode obj = new ObjectNode(JsonNodeFactory.instance);
            obj.put("error", e.getMessage());
            obj.put("statusCode", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            respBuilder.data(obj);
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }

}
