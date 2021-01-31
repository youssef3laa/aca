package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.cs.AppworkCSOperations;
import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.CreateNode;
import com.asset.appwork.dto.Document;
import com.asset.appwork.dto.UploadNodeAndSetCategory;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //    @PostMapping("/upload")
//    public ResponseEntity<AppResponse<JsonNode>> uploadFile(@RequestHeader("X-Auth-Token") String token,
//                                                            @RequestParam("file") MultipartFile file,
//                                                            @RequestParam("parentId") Long parentId,
//                                                            @RequestParam("name") String name) {

    @PostMapping("/upload")
    public ResponseEntity<AppResponse<Document>> uploadFile(@RequestHeader("X-Auth-Token") String token,
                                                            CreateNode createNode) {
        AppResponse.ResponseBuilder<Document> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            try {
//                CreateNode createNode = new CreateNode();
//                createNode.setFile(file.getBytes());
//                createNode.setName(name);
//                createNode.setParentId(parentId);
                createNode.setType(144);

                //TODO move below code to service
                Document resultDocument = appworkCSOperations.checkIfDocumentAlreadyExists(createNode.getParent_id(),
                        createNode.getName(),
                        144);
                if (resultDocument != null) {
//                    document.setId(nodeId);
                    respBuilder.status(ResponseCode.SUCCESS);
                    respBuilder.data(resultDocument);
                    return respBuilder.build().getResponseEntity();
                }
                Document document = new Document();
                Http http = appworkCSOperations.
                        uploadDocument(createNode);
                System.out.println(http.getResponse());
                System.out.println(http.getStatusCode());
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
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
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
//                CreateNode createNode = new CreateNode();
//                createNode.setFile(file.getBytes());
//                createNode.setName(name);
//                createNode.setParentId(parentId);
                createNode.setType(0);


                // TODO move below code to service
                // TODO to be refactored

                Document document = appworkCSOperations.checkIfDocumentAlreadyExists(createNode.getParent_id(),
                        createNode.getName(),
                        0);
                if (document != null) {
                    respBuilder.status(ResponseCode.SUCCESS);
                    respBuilder.data(document);
                    return respBuilder.build().getResponseEntity();
                }
                Http http = appworkCSOperations.
                        uploadDocument(createNode);
                System.out.println(http.getResponse());
                System.out.println(http.getStatusCode());
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
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
//            try {
//                respBuilder.data(new ObjectMapper().readTree(e.getMessage()));
//            } catch (JsonProcessingException jsonProcessingException) {
//                jsonProcessingException.printStackTrace();
//            }

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
//                Document document=
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
            Http http = appworkCSOperations.updateCategoryOnNode(nodeId, categoryId, categoryValues);
            respBuilder.status(SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
            respBuilder.data(SystemUtil.convertStringToJsonNode(http.getResponse()));
            log.info("updatedNodeOnDocument: " + http.getResponse());
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
                                                                               UploadNodeAndSetCategory uploadNodeAndSetCategory) {

        AppResponse.ResponseBuilder<Document> respBuilder = AppResponse.builder();
        try {
            Account account = tokenService.get(token);
            if (account == null) throw new AppworkException(ResponseCode.UNAUTHORIZED);
            AppworkCSOperations appworkCSOperations = new AppworkCSOperations(account.getUsername(), account.getPassword());
            uploadNodeAndSetCategory.setType(144);

            //TODO move below code to service
            Document document = appworkCSOperations.checkIfDocumentAlreadyExists(uploadNodeAndSetCategory.getParent_id(),
                    uploadNodeAndSetCategory.getName(),
                    144);
            if (document != null) {
                ObjectMapper mapper = new ObjectMapper();
                //TODO optimize
                Map<String, String> result = mapper.convertValue(mapper.readTree(uploadNodeAndSetCategory.getCategoriesMap()), new TypeReference<>() {
                });
                appworkCSOperations.updateCategoryOnNode(document.getProperties().getId(), uploadNodeAndSetCategory.getCategory_id(), result);
                respBuilder.status(ResponseCode.SUCCESS);
                respBuilder.data(document);
                return respBuilder.build().getResponseEntity();
            }

            CreateNode createNode = new CreateNode();
            createNode.setType(144);
            createNode.setName(uploadNodeAndSetCategory.getName());
            createNode.setFile(uploadNodeAndSetCategory.getFile());
            createNode.setParent_id(uploadNodeAndSetCategory.getParent_id());
            Http http = appworkCSOperations.
                    uploadDocument(createNode);
            System.out.println(http.getResponse());
            System.out.println(http.getStatusCode());

            ObjectMapper mapper = new ObjectMapper();
            Document documentResult = mapper.treeToValue(mapper.readTree(http.getResponse()).get("results").get("data"), Document.class);

            //TODO optimize
            Map<String, String> result = mapper.convertValue(mapper.readTree(uploadNodeAndSetCategory.getCategoriesMap()), new TypeReference<>() {
            });

            documentResult.setCategories(List.of((LinkedHashMap<String, String>) result));
            appworkCSOperations.updateCategoryOnNode(documentResult.getProperties().getId(), uploadNodeAndSetCategory.getCategory_id(), result);
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

//    uploadThenUpdateCategory


}
