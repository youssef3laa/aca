package com.asset.appwork.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by karim on 10/26/20.
 */
public class SystemUtil {
    public static String readJSONField(String json, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        return (jsonNode == null ? null : jsonNode.textValue());
    }

}
