package com.asset.appwork.util;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
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
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.Text;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.env.Environment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * Created by karim on 10/26/20.
 */
public class SystemUtil {

    public static void exportJsonToDocx(String json, String html, String fileName) {
        File file = new File(fileName + ".docx");
        JacksonJsonParser jackson = new JacksonJsonParser();
        List list = jackson.parseList(json);
        String jsonString = list.toString();

        try {
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();

            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            wordPackage.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();
            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordPackage);
            XHTMLImporter.setHyperlinkStyle("Hyperlink");
            wordPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(html, null));
            wordPackage.save(file);

            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            //mainDocumentPart.addStyledParagraphOfText("Title", "Hello World!");
            mainDocumentPart.addParagraphOfText(jsonString);
            wordPackage.save(file);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (Docx4JException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static String importDocxToJson(String fileName) {
        String text = "";
        try {
            File file = new File(fileName + ".docx");
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(file);
            MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
            String textNodesXPath = "//w:t";
            List<Object> textNodes = mainDocumentPart.getJAXBNodesViaXPath(textNodesXPath, true);
            for (Object obj : textNodes) {
                Text tempText = (Text) ((JAXBElement) obj).getValue();
                String textValue = tempText.getValue();
                text.concat(textValue + "/n");
            }
            System.out.println(text);
        } catch (Docx4JException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static ResponseCode getResponseCodeFromInt(Integer code) {
        if(code == null) return null;
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

    public static List<?> readJSONArray(String json, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        if(jsonNode != null){
            if(jsonNode.isArray()){
                return mapper.convertValue(jsonNode, ArrayList.class);
            }else{
                ArrayList<LinkedHashMap> arrayList = new ArrayList<>();
                arrayList.add(mapper.convertValue(jsonNode,LinkedHashMap.class));
                return arrayList;
            }
        }
        return null;
    }

    public static <T> List<T> readJSONArray(String json, String name, Class<T> type) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        if(jsonNode != null){
            if(jsonNode.isArray()){
                return mapper.convertValue(jsonNode, mapper.getTypeFactory().constructCollectionType(List.class, type));
//                return mapper.convertValue(jsonNode, List.class);
            }else{
                ArrayList<T> arrayList = new ArrayList<>();
                arrayList.add(mapper.convertValue(jsonNode,type));
                return arrayList;
            }
        }
        return null;
    }

    public static String readJSONObject(String json, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        return (jsonNode == null ? null : jsonNode.toPrettyString());
    }

    public static Object readJSONObject(String json, String name, Class objectClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        jsonNode = jsonNode.get(name);
        return (jsonNode == null ? null : mapper.convertValue(jsonNode, objectClass));
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

    public static String convertJSONtoXML(String json) throws AppworkException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectMapper xmlMapper = new XmlMapper();
            JsonNode tree = objectMapper.readTree(json);
            String xml = xmlMapper.writer().withoutRootName().writeValueAsString(tree);
            return xml.replace("<>", "").replace("</>", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new AppworkException(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> String convertObjectToXML(T object) throws AppworkException {
        return convertObjectToXML(object, "");
    }

    public static <T> String convertObjectToXML(T object, String rootName) throws AppworkException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectMapper xmlMapper = new XmlMapper();
            JsonNode tree = objectMapper.readTree(objectMapper.writeValueAsString(object));
            return xmlMapper.writer().withRootName(rootName).writeValueAsString(tree).replace("<>", "").replace("</>", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new AppworkException(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR);
        }
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

    public static String getJsonByPtrExpr(String json, String jsonPtrExpr) {
        try {
            return  new ObjectMapper().readTree(json).at(jsonPtrExpr).asText();
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static String getJsonObjectByPtrExpr(String json, String jsonPtrExpr) {
        try {
            return  new ObjectMapper().readTree(json).at(jsonPtrExpr).toString();
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static Boolean isFieldInJson(String json, String fieldName) {
        try {
            return new ObjectMapper().readTree(json).has(fieldName);
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public static String generateOtdsAPIBaseUrl(Environment env) {
        return String.format("%s://%s:%s/otdsws/rest", env.getProperty("otds.request"), env.getProperty("otds.domain"),
                env.getProperty("otds.port"));
    }

    public static String generateOtdsRoleUserCN(Environment env, String name) {
        return String.format("cn=%s,ou=Root,ou=%s,ou=IdentityProviders,dc=identity,dc=opentext,dc=net", name, env.getProperty("otds.partition"));
    }

    public static String generatePlatformRoleCN(Environment env, String roleName) {
        return String.format("cn=%s,cn=organizational roles,o=%s,cn=cordys,cn=defaultInst,o=appworks-aca.local", roleName, env.getProperty("appwork.organization"));
    }

    public static String generateRestAPIBaseUrl(Environment env, String solution) {
        return String.format("%s://%s:%s/home/%s/app/entityRestService/api/%s", env.getProperty("server.request"),
                env.getProperty("appwork.domain"), env.getProperty("appwork.port"), env.getProperty("appwork.organization"),
                solution);
    }

    public static String readFile(String rootPath) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        String line;
        try (FileReader fr = new FileReader(rootPath);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                fileContent.append(line);
                fileContent.append(System.lineSeparator());
            }

        }
        return fileContent.toString();
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
