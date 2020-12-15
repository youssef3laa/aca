package com.asset.appwork.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

    public static JsonNode convertStringToJsonNode(String json) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        return json != null ? mapper.readTree(json) : null;
    }

    public static List<?> readJSONArray(String json, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        return (jsonNode == null ? null : mapper.convertValue(jsonNode, ArrayList.class));
    }

    public static String readJSONObject(String json, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        return (jsonNode == null ? null : jsonNode.toPrettyString());
    }

    public static String writeObjectIntoString(Object object) {
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
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

    public static String convertXMLtoJSON(String xml) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(xml.getBytes());
            ObjectMapper jsonMapper = new ObjectMapper();
            String json = jsonMapper.writeValueAsString(node);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public static String convertDocumentNodetoJSON(Node node) {
        String xml = convertXMLDocumentNodeToString(node);
        String json = convertXMLtoJSON(xml);
        return json;
    }
}
