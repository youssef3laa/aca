package com.asset.appwork.util;

import com.asset.appwork.dto.Memos;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.MemoValuesRepository;
import com.asset.appwork.repository.MemosRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Bassel on 19/01/2021.
 */
@Slf4j
@Service
public class Docx {
    @Autowired
    MemosRepository memosRepository;
    @Autowired
    MemoValuesRepository memoValuesRepository;
    @Autowired
    Environment env;

    public File exportJsonToDocx(Memos memo, User user) throws AppworkException {
        try {
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            wordPackage.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();
            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordPackage);
            XHTMLImporter.setHyperlinkStyle("Hyperlink");

            JsonNode jsonNode = null;
            try {//<iframe src="http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&amp;nodeid=730301&amp;viewType=1&amp;OTDSTicket=*OTDSSSO*AXhBQlRqZjBLaThfcEtXdVVqLUo0dmdnQV81UTdFZ3dBUVRxbWRQNF9ZMGZKWXpSdjVLUUg5SGdEdzlDU2lYbUhjem9MS08yQTUxOGx1SkJfbUlEemMzbHhVNHpMcTZpUmFieklLY3JlMWlKdTRxZi1rSm9ZcHJNUWpsYXNQUkdrQjZOcEZXdUJBU0owbkl1YkhUa2JsSHVCbmFiSDMxQWtMbHBRdUtGRFB1ZFlzeWx2eEFGWXVhOHAxZ2k5a2NmT3RjMGNSNTdpTGpEclRDb1p3RXpuZUhtem5ScU9rZ3Q2RHJqbXdIRXZybFpYWkJ6c09BR295VVBieTJpVndfa2VUYVdxUFMxX1N1Ql83NUhwcnNrb3psLUVISC1PbVRPRmVHandBZ2dkczAzelNremdpNHdfZW5VZXZ4aDhiNTV2Z0o4RExVbjVhblQzdm9OeEQ0RmVVOHo0YmNlYTV4MzBWbHFSaXE4bTR4dG5odXR2RnpvcnByM1pKAE4ASgAU6Kg-_nQYvkr-yBVhvvHDzVKVes4AEFqGcE-28Fv5ID3bwyUXiKUAIPtbzpFAz0_64zbZ6K1oisKPpcxifBLPLpD1l5ZAVgxhAAA*" style="border-width: 0px;padding: 0px;margin: 0px;overflow: hidden;width: 100%;height: 1000px;"></iframe>
                jsonNode = SystemUtil.convertStringToJsonNode(SystemUtil.readFile(System.getProperty("user.dir") + env.getProperty("memosPath") + memo.getJsonId() + ".json"));
                jsonNode = jsonNode.get("app").get("pages").get("page").get(0).get("sections").get("sec").get(0).get("forms");
                List<String> sections = new ArrayList<>();
                for(int i = 0; i < jsonNode.size(); i++){
                    sections.add(jsonNode.get(i).get("name").asText());
                }

                HashMap<String, String> memoValues = memo.getValues();
                Object[] memoValuesObjectArray = memoValues.values().toArray();
                String[] memoValuesStringArray = Arrays.copyOf(memoValuesObjectArray, memoValuesObjectArray.length, String[].class);
                String value = "";
                for(int i = 0; i < memoValuesStringArray.length; i++){
                    memoValuesStringArray[i] = memoValuesStringArray[i].replaceAll("&nbsp;", "");
                    memoValuesStringArray[i] = memoValuesStringArray[i].replaceAll("-&nbsp;", "");
                    value += "<p style = 'font-size: 50px; font-weight: bold; text-align:right;'>" + sections.get(i) + "</p>" + "<br>";
                    value += memoValuesStringArray[i] + "<br>";
                }

                Document doc = Jsoup.parse(value);

                Optional<Group> group = user.getGroup().stream().findFirst();
                String groupCode = "";
                if(group.isPresent())
                {
                    groupCode = group.get().getGroupCode();
                }

                String signature = "<img src=\"https://i.ibb.co/h94n9bR/signature.png\"></img>";
                String name = "<span>" + reverseString(user.getDisplayName()) + "</span>";
                try {
                    doc.select("#" + groupCode + "signature").first().removeAttr("hidden").append(signature);
                    doc.select("#" + groupCode + "name").first().removeAttr("hidden").append(name);
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                    log.error("Docx: " + e.getMessage());
                    throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
                }
                value = doc.toString();

                if (value.contains("<img"))
                {
                    int imageIndex = value.indexOf("<img");
                    int imageEndIndex = value.indexOf(">", imageIndex) + 1;
                    StringBuilder stringBuilder = new StringBuilder(value);
                    stringBuilder.insert(imageEndIndex, "</img>");
                    value = stringBuilder.toString();
                }
                value = value.replaceAll("<br>", "<br></br>");
                wordPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(value, null));

            } catch (IOException e) {
                e.printStackTrace();
                log.error("Docx: " + e.getMessage());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
            Date date = new Date();

            if (!Files.isDirectory(Paths.get("Temp"))) {
                File theDir = new File("Temp");
                theDir.mkdirs();
            }
            File file = new File("Temp" + File.separator + "Memo" + memo.getRequestId() + " " + dateFormat.format(date) + ".docx");
            wordPackage.save(file);
            return file;

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (Docx4JException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (JAXBException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public String reverseString(String str)
    {
        String[] words = str.split(" ");
        String reversedString = "";
        for (int i = words.length - 1; i >= 0; i--)
        {
            reversedString += words[i] + " ";
        }
        return reversedString;
    }

    public String importDocxToJson(String fileName) throws AppworkException {
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
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (JAXBException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return text;
    }
}
