package com.asset.appwork.util;

import com.asset.appwork.enums.ResponseCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by karim on 10/26/20.
 */
public class SystemUtil {

    public static ResponseCode getResponseCodeFromInt(Integer code) throws NullPointerException {
        for (ResponseCode c : ResponseCode.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        return null;
    }

    public static String readJSONField(String json, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        return (jsonNode == null ? null : jsonNode.textValue());
    }

    public static JsonNode convertStringToJsonNode(String json) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        return json != null ? mapper.readTree(json) : null;
    }

    public static String readJSONObject(String json, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        return (jsonNode == null ? null : jsonNode.toPrettyString());
    }

    public static Document convertStringToXMLDocument(String data) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(data)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertXMLDocumentNodeToString(Node node) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            String xml = writer.toString();
            return xml.substring(xml.indexOf("?>") + 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String convertXMLtoJSON(String xml) {
//        try {
//            XmlMapper xmlMapper = new XmlMapper();
//            JsonNode node = xmlMapper.readTree(xml.getBytes());
//            ObjectMapper jsonMapper = new ObjectMapper();
//            String json = jsonMapper.writeValueAsString(node);
//            return json;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static String convertJSONtoXML(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectMapper xmlMapper = new XmlMapper();
            JsonNode tree = objectMapper.readTree(json);
            String xml = xmlMapper.writer().withoutRootName().writeValueAsString(tree);
            return xml.replace("<>", "").replace("</>", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addNameSpaceToXML(String xml, String nameSpace) {
        if (!nameSpace.isEmpty() && !xml.isEmpty()) {
            return xml.replaceFirst(">", " xmlns=\"" + nameSpace + "\">");
        }
        return xml;
    }

    public static String convertDocumentNodetoJSON(Node node) throws IOException {
        String xml = convertXMLDocumentNodeToString(node);
        String json = convertXMLtoJSON(xml);
        return json;
    }

    public static String convertXMLtoJSON(String xml) throws IOException {
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(new XmlMapper().registerModule(new SimpleModule().addDeserializer(Object.class, new SystemUtil.FixedUntypedObjectDeserializer())).readValue(xml, Object.class));
    }

    public static String getJsonByPtrExpr(String json, String jsonPtrExpr) throws IOException {
        return new ObjectMapper().readTree(json).at(jsonPtrExpr).asText();
    }

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


}
