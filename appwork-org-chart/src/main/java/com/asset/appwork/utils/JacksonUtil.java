package com.asset.appwork.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

// Taken from https://gist.github.com/joaovarandas/1543e792ed6204f0cf5fe860cb7d58ed as a temp solution
public class JacksonUtil {
    public static class FixedUntypedObjectDeserializer extends UntypedObjectDeserializer {

        @Override
        protected Object mapObject(JsonParser p, DeserializationContext ctxt) throws IOException {
            String firstKey;
            JsonToken t = p.getCurrentToken();

            if (t == JsonToken.START_OBJECT) {
                firstKey = p.nextFieldName();
            } else if (t == JsonToken.FIELD_NAME) {
                firstKey = p.getCurrentName();
            } else {
                if (t != JsonToken.END_OBJECT) {
                    throw ctxt.mappingException(handledType(), p.getCurrentToken());
                }
                firstKey = null;
            }

            // empty map might work; but caller may want to modify... so better
            // just give small modifiable
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>(2);
            if (firstKey == null)
                return resultMap;

            p.nextToken();
            resultMap.put(firstKey, deserialize(p, ctxt));

            // 03-Aug-2016, jpvarandas: handle next objects and create an array
            Set<String> listKeys = new LinkedHashSet<>();
            String nextKey;

            while ((nextKey = p.nextFieldName()) != null) {
                p.nextToken();
                if (resultMap.containsKey(nextKey)) {
                    Object listObject = resultMap.get(nextKey);

                    if (!(listObject instanceof List)) {
                        listObject = new ArrayList<>();
                        ((List) listObject).add(resultMap.get(nextKey));

                        resultMap.put(nextKey, listObject);
                    }

                    ((List) listObject).add(deserialize(p, ctxt));
                    listKeys.add(nextKey);

                } else {
                    resultMap.put(nextKey, deserialize(p, ctxt));
                }
            }
            return resultMap;
        }
    }

    public static String XmlToJsonString(String xml) throws IOException {
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(new XmlMapper().registerModule(new SimpleModule().addDeserializer(Object.class, new JacksonUtil.FixedUntypedObjectDeserializer())).readValue(xml, Object.class));
    }

    public static String getJsonFieldByPath(String json, String path) throws IOException {
        return new ObjectMapper().readTree(json).at(path).asText();
    }
}