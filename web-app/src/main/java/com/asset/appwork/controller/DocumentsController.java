package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cs.AppworkCSOperations;
import com.asset.appwork.dto.*;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.AttachmentSort;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.AttachmentSortService;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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

    @Autowired
    Environment environment;

    @PostMapping("/upload")
    public ResponseEntity<AppResponse<Document>> uploadFile(@RequestHeader("X-Auth-Token") String token,
                                                            CreateNode createNode) {
        AppResponse.ResponseBuilder<Document> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            try {
                createNode.setType(144);
                Document document;
                Http http = appworkCSOperations.
                        uploadDocument(createNode);
                ObjectMapper mapper = new ObjectMapper();
                document = mapper.treeToValue(mapper.readTree(http.getResponse()).get("results").get("data"), Document.class);
                respBuilder.status(ResponseCode.SUCCESS);
                respBuilder.data(document);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                respBuilder.status(ResponseCode.BAD_REQUEST);

            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @PostMapping("/create/folder")
    public ResponseEntity<AppResponse<Document>> createFolder(@RequestHeader("X-Auth-Token") String token,
                                                              CreateNode createNode) {
        AppResponse.ResponseBuilder<Document> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            try {
                createNode.setType(0);

                Document existedDocument = appworkCSOperations.checkIfDocumentAlreadyExists(createNode.getParent_id(),
                        createNode.getName(),
                        createNode.getType());
                if (existedDocument != null) {
                    respBuilder.data(existedDocument);
                    respBuilder.status(ResponseCode.SUCCESS);
                    return respBuilder.build().getResponseEntity();
                }

                Http http = appworkCSOperations.
                        uploadDocument(createNode);
                ObjectMapper mapper = new ObjectMapper();
                Document document = mapper.treeToValue(mapper.readTree(http.getResponse()).get("results").get("data"), Document.class);
                respBuilder.status(ResponseCode.SUCCESS);
                respBuilder.data(document);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                respBuilder.status(ResponseCode.BAD_REQUEST);
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        } catch (AppworkException e) {
            log.error(e.getMessage());
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<AppResponse<byte[]>> downloadFile(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("documentId") Long documentId) {
        AppResponse.ResponseBuilder<byte[]> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());

            Http http = appworkCSOperations.downloadDocument(documentId);

            byte[] file = http.getResponseFile();

            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(file);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            respBuilder.status(e.getCode());
        }

        return respBuilder.build().getResponseEntity();
    }

    @GetMapping("/list/{parentId}")
    public ResponseEntity<AppResponse<List<Document>>> listSubNodes(@RequestHeader("X-Auth-Token") String token,
                                                                    @PathVariable("parentId") Long parentId,
                                                                    AppworkCSOperations.DocumentQuery documentQuery
    ) {

        //TODO handle if properties is excluded from document query
        AppResponse.ResponseBuilder<List<Document>> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.getSubNodes(parentId, documentQuery);
            respBuilder.status(ResponseCode.SUCCESS);

            ObjectMapper mapper = new ObjectMapper();
            Iterator<JsonNode> results = mapper.readTree(http.getResponse()).withArray("results").elements();
            List<Document> documentList = new ArrayList<>();
            while (results.hasNext()) {
                JsonNode dataNode = results.next();
                documentList.add(mapper.treeToValue(dataNode.get("data"), Document.class));
            }
            respBuilder.data(documentList);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());

        } catch (JsonProcessingException | IllegalAccessException e) {
            e.printStackTrace();
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
            appworkCSOperations.deleteNode(nodeId);
            respBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
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
    public ResponseEntity<AppResponse<Document>> listNodeVersions(@RequestHeader("X-Auth-Token") String token,
                                                                  @PathVariable("nodeId") Long nodeId,
                                                                  AppworkCSOperations.DocumentQuery documentQuery) {

        AppResponse.ResponseBuilder<Document> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            Http http = appworkCSOperations.getNodeVersions(nodeId, documentQuery);
            ObjectMapper mapper = new ObjectMapper();
            Iterator<JsonNode> results = mapper.readTree(http.getResponse()).withArray("results").elements();
            List<Version> versionList = new ArrayList<>();
            while (results.hasNext()) {
                JsonNode dataNode = results.next();
                Version version = mapper.treeToValue(dataNode.get("data").get("versions"), Version.class);
                versionList.add(version);
            }
            Document document = new Document();
            document.setVersions(versionList);
            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(document);
        } catch (IllegalAccessException | JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            respBuilder.status(e.getCode());
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


    @PostMapping("version/{nodeId}")
    public ResponseEntity<AppResponse<String>> addNodeVersion(@RequestHeader("X-Auth-Token") String token,
                                                              @NonNull @PathVariable("nodeId") Long nodeId,
                                                              @NonNull @RequestParam("file") MultipartFile file) {

        AppResponse.ResponseBuilder<String> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            appworkCSOperations.addNodeVersion(nodeId, file);
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return responseBuilder.build().getResponseEntity();
    }

    //categories
    @PutMapping("{id}/categories/{categoryId}")
    public ResponseEntity<AppResponse<JsonNode>> updateCategoryOnNode(@RequestHeader("X-Auth-Token") String token,
                                                                      @RequestBody HashMap<String, String> categoryValues,
                                                                      @PathVariable("id") Long nodeId,
                                                                      @PathVariable("categoryId") Long categoryId) {

        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            appworkCSOperations.updateCategoryOnNode(nodeId, categoryId, categoryValues);
            respBuilder.status(ResponseCode.SUCCESS);
            log.info("updatedNodeOnDocument: " + nodeId);
        } catch (AppworkException e) {
            log.error(e.getMessage());
            respBuilder.status(e.getCode());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return respBuilder.build().getResponseEntity();
    }


    //Multiple operations
    @PostMapping("uploadAndSetCategory")
    public ResponseEntity<AppResponse<Document>> uploadAndUpdateCategoryOnNode(@RequestHeader("X-Auth-Token") String token,
                                                                               UploadNodeAndSetCategory uploadNodeAndSetCategory,
                                                                               AppworkCSOperations.DocumentQuery documentQuery) {

        AppResponse.ResponseBuilder<Document> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            uploadNodeAndSetCategory.setType(144);

            CreateNode createNode = new CreateNode();
            createNode.setType(144);
            createNode.setName(uploadNodeAndSetCategory.getName());
            createNode.setFile(uploadNodeAndSetCategory.getFile());
            createNode.setParent_id(uploadNodeAndSetCategory.getParent_id());
            createNode.setCategory_id(uploadNodeAndSetCategory.getCategory_id());
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> result = mapper.convertValue(mapper.readTree(uploadNodeAndSetCategory.getCategoriesMap()), new TypeReference<>() {
            });
            Document documentResult = appworkCSOperations.uploadNodeAndSetCategory(createNode, documentQuery, (LinkedHashMap<String, String>) result);
            respBuilder.status(ResponseCode.SUCCESS);
            respBuilder.data(documentResult);
        } catch (AppworkException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            respBuilder.status(e.getCode());
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return respBuilder.build().getResponseEntity();
    }
}
