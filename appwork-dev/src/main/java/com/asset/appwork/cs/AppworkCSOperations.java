package com.asset.appwork.cs;


import com.asset.appwork.dto.CreateNode;
import com.asset.appwork.dto.Document;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AppworkCSOperations {

    // content server host
    private static final String CS_HOST = "http://45.240.63.94/otcs/cs.exe/";
    private static final String API_VER = "api/v2/";
    private static final String API_ROOT = "nodes/";
    private final String userName;
    private final String password;


    public AppworkCSOperations(String userName, String password) {
        //TODO use arguments
        this.userName = "admin";
        this.password = "Asset99a";
    }

    //document management
    public Http getNodeDetails(Long nodeId, DocumentQuery documentQuery) throws AppworkException, IllegalAccessException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.NODE_ACTION.getApiURL());
        uriComponentsBuilder.path("/" + nodeId);
        fillURIComponentWithQuery(uriComponentsBuilder, documentQuery);

        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().build().toString());
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }

    public void deleteNode(Long nodeId) throws AppworkException {
        String urlStr = CS_API.NODE_ACTION.getApiURL() + "/" +
                nodeId;
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .delete(urlStr);
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
    }

    public Http uploadDocument(CreateNode createNode) throws AppworkException, IllegalAccessException, NoSuchFieldException, IOException {
        if (createNode.getName() == null || createNode.getName().length() == 0)
            createNode.setName(createNode.getFile().getOriginalFilename());


        Class<CreateNode> createNodeClass = CreateNode.class;


        Field[] fields = createNodeClass.getDeclaredFields();
        List<Part> parts = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(createNode);
            if (o == null) continue;

            if (o instanceof MultipartFile) {
                Field fileName = createNodeClass.getDeclaredField("name");
                fileName.setAccessible(true);
                byte[] bytes = ((MultipartFile) o).getBytes();
                parts.add(new FilePart(field.getName(),
                        new ByteArrayPartSource((String) fileName.get(createNode),
                                bytes)));
            } else {
                parts.add(new StringPart(field.getName(), String.valueOf(o), StandardCharsets.UTF_8.name()));
            }
        }
        String urlStr = CS_API.NODE_ACTION.getApiURL();
        Part[] partsArray = new Part[parts.size()];
        partsArray = parts.toArray(partsArray);
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.FORM_REQUEST)
                .setData(partsArray)
                .post(urlStr);
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), ResponseCode.INTERNAL_SERVER_ERROR);

        return http;
    }

    public Http getSubNodes(Long parentId, DocumentQuery documentQuery) throws AppworkException, IllegalAccessException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_SUB_NODES.getApiURL());
        fillURIComponentWithQuery(uriComponentsBuilder, documentQuery);

        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", String.valueOf(parentId));
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());

        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }

    public Http downloadDocument(Long documentId) throws AppworkException {
        String urlStr = CS_API.NODE_ACTION.getApiURL() + documentId + "/content";
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .download(urlStr);
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));

        return http;
    }

    //versioning
    public Http getNodeVersions(Long nodeId, DocumentQuery documentQuery) throws IllegalAccessException, AppworkException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_NODES_VERSIONS.getApiURL());
        fillURIComponentWithQuery(uriComponentsBuilder, documentQuery);
//        Class<DocumentQuery> cls = DocumentQuery.class;
//        Field[] fields = cls.getDeclaredFields();
//        String fieldName;
//        for (Field field : fields) {
//            fieldName = field.getName();
//            JsonProperty[] jsonProperties = field.getAnnotationsByType(JsonProperty.class);
//            if (jsonProperties.length > 0) fieldName = jsonProperties[0].value();
//
//            uriComponentsBuilder.queryParamIfPresent(fieldName, Optional.ofNullable(field.get(documentQuery)));
//        }
        System.out.println(uriComponentsBuilder);
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", nodeId);

        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), ResponseCode.INTERNAL_SERVER_ERROR);
        return http;
    }

    public Http getSpecifiedNodeVersion(Long nodeId, Integer versionId, DocumentQuery documentQuery) throws IllegalAccessException, AppworkException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_SPECIFIED_NODE_VERSION.getApiURL());
        fillURIComponentWithQuery(uriComponentsBuilder, documentQuery);
        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("id", nodeId);
        pathVariables.put("version_number", versionId);
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());
        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), SystemUtil.getResponseCodeFromInt(http.getStatusCode()));
        return http;
    }


    //categories
    public void updateCategoryOnNode(Long nodeId, Long categoryId, Map<String, String> categoryValues) throws AppworkException, JsonProcessingException {


        List<Document> categoriesListOnNode = getCategoriesOnNode(nodeId);
        boolean categoryAppliedOnNode = false;
        for (Document categoryOnDocument :
                categoriesListOnNode) {
            Set<String> set = categoryOnDocument.getCategories().get(0).keySet()
                    .stream()
                    .filter(s -> s.startsWith(categoryId.toString()))
                    .collect(Collectors.toSet());

            if (!set.isEmpty()) {
                categoryAppliedOnNode = true;
                break;
            }
        }
        if (!categoryAppliedOnNode) {
            applyCategoryOnNode(nodeId, categoryId, categoryValues);
            return;
        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.UPDATE_CATEGORY_ON_NODE.getApiURL());
        Map<String, Object> pathVariables = new HashMap<>();

        pathVariables.put("id", nodeId);
        pathVariables.put("category_id", categoryId);
        Part[] parts = new Part[categoryValues.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : categoryValues.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
            parts[index] = new StringPart(entry.getKey(), entry.getValue(), StandardCharsets.UTF_8.name());
            ++index;
        }
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setData(parts)
                .setContentType(Http.ContentType.FORM_REQUEST)
                .put(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());

        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), ResponseCode.INTERNAL_SERVER_ERROR);
    }

    public List<Document> getCategoriesOnNode(Long nodeId) throws AppworkException, JsonProcessingException {
        UriComponentsBuilder listCatOnNodesUriComponentBuilder = UriComponentsBuilder.fromUriString(CS_API.LIST_CATEGORIES_ON_NODE.getApiURL());
        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("id", nodeId);

        Http listCatOnNodeHttp = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(listCatOnNodesUriComponentBuilder.encode().buildAndExpand(pathVariables).toString());
        if (!listCatOnNodeHttp.isSuccess())
            throw new AppworkException(listCatOnNodeHttp.getResponse(), ResponseCode.INTERNAL_SERVER_ERROR);
        ObjectMapper objectMapper = new ObjectMapper();
        Iterator<JsonNode> results = objectMapper.readTree(listCatOnNodeHttp.getResponse()).withArray("results").elements();
        List<Document> documentList = new ArrayList<>();
        while (results.hasNext()) {
            JsonNode dataNode = results.next();
            Document document = new Document();

            List<LinkedHashMap<String, String>> tempListHashMap = new ArrayList<>();
            Iterator<Map.Entry<String, JsonNode>> nodes = dataNode.get("data").get("categories").fields();
            LinkedHashMap<String, String> stringLinkedHashMap = new LinkedHashMap<>();

            while (nodes.hasNext()) {
                Map.Entry<String, JsonNode> entry = nodes.next();
                stringLinkedHashMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            tempListHashMap.add(stringLinkedHashMap);
            document.setCategories(tempListHashMap);
            documentList.add(document);
        }
        return documentList;
    }

    public void applyCategoryOnNode(Long nodeId, Long categoryId, Map<String, String> categoryValues) throws AppworkException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.LIST_CATEGORIES_ON_NODE.getApiURL());
        Map<String, Object> pathVariables = new HashMap<>();

        pathVariables.put("id", nodeId);
        categoryValues.put("category_id", categoryId.toString());
        Part[] parts = new Part[categoryValues.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : categoryValues.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
            parts[index] = new StringPart(entry.getKey(), entry.getValue(), StandardCharsets.UTF_8.name());
            ++index;
        }
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setData(parts)
                .setContentType(Http.ContentType.FORM_REQUEST)
                .post(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());

        if (!http.isSuccess())
            throw new AppworkException(http.getResponse(), ResponseCode.INTERNAL_SERVER_ERROR);

    }

    //general
    public void fillURIComponentWithQuery(UriComponentsBuilder uriComponentsBuilder, DocumentQuery documentQuery) throws IllegalAccessException {
        if (documentQuery == null || uriComponentsBuilder == null) return;
        Class<DocumentQuery> cls = DocumentQuery.class;
        Field[] fields = cls.getDeclaredFields();
        String fieldName;
        for (Field field : fields) {
            fieldName = field.getName();
            JsonProperty[] jsonProperties = field.getAnnotationsByType(JsonProperty.class);
            if (jsonProperties.length > 0) fieldName = jsonProperties[0].value();
            Optional<Object> o = Optional.ofNullable(field.get(documentQuery));
            if (o.isPresent() && o.get().getClass().isArray()) {

                Object[] objects = SystemUtil.unpackObjectToArrayOfObjects(o.get());
                for (Object object :
                        objects) {
                    uriComponentsBuilder.queryParam(fieldName, object);
                }
            } else {
                uriComponentsBuilder.queryParamIfPresent(fieldName, Optional.ofNullable(field.get(documentQuery)));
            }
        }
    }

    public Document checkIfDocumentAlreadyExists(Long ParentId, String name, Integer type) throws IllegalAccessException, AppworkException, JsonProcessingException {

//        {{baseUrl}}/v2/nodes/:id/nodes?where_type=0&where_name=testrefaii21&fields=properties{id,name,name_multilingual}
        // TODO to be refactored
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(CS_API.GET_SUB_NODES.getApiURL());
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setWhere_type(String.valueOf(type));
        documentQuery.setWhere_name(name);
//        documentQuery.setFields("properties");
        fillURIComponentWithQuery(uriComponentsBuilder, documentQuery);
        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("id", ParentId);
        Http http = new Http().setDoAuthentication(true)
                .basicAuthentication(this.userName, this.password)
                .setContentType(Http.ContentType.JSON_REQUEST)
                .get(uriComponentsBuilder.encode().buildAndExpand(pathVariables).toString());

        if (!http.isSuccess()) throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(http.getResponse());
        ArrayNode jsonArray = jsonNode.withArray("results");

        if (jsonArray.size() == 0) return null;
        if (jsonArray.size() == 1) {
            Http nodeDetailsHttp = getNodeDetails(Long.valueOf(jsonArray.get(0).get("data").get("properties").get("id").textValue()), documentQuery);
            return objectMapper.treeToValue(objectMapper.readTree(nodeDetailsHttp.getResponse()).get("results").get("data"), Document.class);
        }
        if (jsonArray.size() > 0)
            throw new AppworkException("there are more than on file with the same name", ResponseCode.DUPLICATION_CONFLICT);
        return null;
    }

    // Multiple operations
    public Document uploadNodeAndSetCategory(CreateNode createNode, DocumentQuery documentQuery, LinkedHashMap<String, String> categoriesLinkedHashMaps) throws IOException, AppworkException, NoSuchFieldException, IllegalAccessException {

        Http http = uploadDocument(createNode);
        ObjectMapper mapper = new ObjectMapper();
        Document documentResult = mapper.treeToValue(mapper.readTree(http.getResponse()).get("results").get("data"), Document.class);

        updateCategoryOnNode(documentResult.getProperties().getId(), createNode.getCategory_id(), categoriesLinkedHashMaps);
        Http nodeDetailsHttp = getNodeDetails(documentResult.getProperties().getId(), documentQuery);
        return mapper.treeToValue(mapper.readTree(nodeDetailsHttp.getResponse()).get("results").get("data"), Document.class);
    }

    public enum CS_API {
        //APIs source URL
        //https://appworksdeveloper.opentext.com/webaccess/#url=%2Fawd%2Fresources%2Fapis%2Fcs-rest-api-for-cs-16-s%23!%2Fnodes&tab=501

        //api/v2/nodes/{id}
        /*
         * get node details
         * upload node
         * */
        NODE_ACTION(),
        //{{baseUrl}}/v2/nodes/:id/nodes
        GET_SUB_NODES("{id}/nodes"),

        //versions
        //{{baseUrl}}/v2/nodes/:id/versions
        GET_NODES_VERSIONS("{id}/versions"),
        //{{baseUrl}}/v2/nodes/:id/versions
        GET_SPECIFIED_NODE_VERSION("{id}/versions/{version_number}"),

        //categories
        UPDATE_CATEGORY_ON_NODE("{id}/categories/{category_id}"),
        LIST_CATEGORIES_ON_NODE("{id}/categories");

        private final String apiURL;

        CS_API() {
            this.apiURL = CS_HOST + API_VER + API_ROOT;
        }

        CS_API(String s) {
            this.apiURL = CS_HOST + API_VER + API_ROOT + s;
        }

        public String getApiURL() {
            return apiURL;
        }

    }

    @Data
    public static class DocumentQuery {
        String where_type;
        String where_name;

        String sort;
        String[] fields;
        String page;
        String limit;
        String order;
        String expand;

    }


}



