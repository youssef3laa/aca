package com.asset.appwork.util;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Memorandum;
import com.asset.appwork.model.memoValues;
import com.asset.appwork.repository.MemoValuesRepository;
import com.asset.appwork.repository.MemosRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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

    public File exportJsonToDocx(String requestId, String fileName) throws AppworkException {
        try {
            /*
                create tempfile
                wordpck.save()=>
                return file
                from controller => upload file on Contentserver handle errors if there are any
            */

            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            wordPackage.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();
            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordPackage);
            XHTMLImporter.setHyperlinkStyle("Hyperlink");

            List<Memorandum> memo = memosRepository.findByRequestId(requestId);
            for (int i = 0; i < memo.size(); i++)
            {
                String jsonId = memo.get(i).getJsonId();
                String memoId = memo.get(i).getId().toString();

                ObjectMapper mapper = new ObjectMapper();
                Map<?, ?> map = mapper.readValue(Paths.get("E:\\aca\\form-config\\output\\" + jsonId + ".json").toFile(), Map.class);
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String json = entry.getKey() + "=" + entry.getValue();

                    int start = json.indexOf("name") + 5;
                    int end = json.indexOf(",", start);
                    String name = json.substring(start, end);
                    mainDocumentPart.addStyledParagraphOfText("Title", name);

                    start = json.indexOf("name", 109) + 5;
                    end = json.indexOf(",", start);
                    String jsonKey = json.substring(start, end);
                    List<memoValues> memoVales = memoValuesRepository.findByMemosIdAndJsonKey(memoId, jsonKey);
                    for (int j = 0; j < memoVales.size(); j++)
                    {
                        wordPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(memoVales.get(j).getValue(), null));
                    }
                }
            }
//            mainDocumentPart.addParagraphOfText(jsonString);
            File file = new File(fileName + ".docx");
            wordPackage.save(file);
            return file;

        } catch (JsonParseException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        } catch (Docx4JException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        } catch (JAXBException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.BAD_REQUEST);
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
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        } catch (JAXBException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
            throw new AppworkException(ResponseCode.BAD_REQUEST);
        }
        return text;
    }
}
