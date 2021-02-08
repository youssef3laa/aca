package com.asset.appwork.util;

import com.asset.appwork.dto.Memos;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Memorandum;
import com.asset.appwork.model.memoValues;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    public File exportJsonToDocx(Memos memo) throws AppworkException {
        try {
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            wordPackage.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();
            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordPackage);
            XHTMLImporter.setHyperlinkStyle("Hyperlink");

            JsonNode jsonNode = null;
            try {
                jsonNode = SystemUtil.convertStringToJsonNode(SystemUtil.readFile(System.getProperty("user.dir") + env.getProperty("memosPath") + memo.getJsonId() + ".json"));
                String name = jsonNode.at("/app/pages/0/sections/0/forms/0/name").asText();
                mainDocumentPart.addStyledParagraphOfText("Title", name);

                Collection<String> memoValues = memo.getValues().values();
                System.out.println("values " + memoValues);
                wordPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n" +
                        "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<body>\n" +
                        memoValues + "\n" +
                        "</body>\n" +
                        "</html>", null));

//                memoValues.forEach((key, value) -> {
//                    try {
//                        wordPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n" +
//                                "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
//                                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
//                                "<body>\n" +
//                                value + "\n" +
//                                "</body>\n" +
//                                "</html>", null));
//                    } catch (Docx4JException e) {
//                        System.out.println("7mada");
//                        e.printStackTrace();
//                        log.error("Docx: " + e.getMessage());
//                    }
//                });
            } catch (IOException e) {
                System.out.println("7mada2");
                e.printStackTrace();
                log.error("Docx: " + e.getMessage());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
            Date date = new Date();
            File file = new File("Memo" + memo.getRequestId() + " " + dateFormat.format(date) + ".docx");
            wordPackage.save(file);
            return file;

        } catch (InvalidFormatException e) {
            System.out.println("7mada3");
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (Docx4JException e) {
            System.out.println("7mada4");
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (JAXBException e) {
            System.out.println("7mada5");
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
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
