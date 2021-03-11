package com.asset.appwork.util;

import com.asset.appwork.dto.Memos;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.IncomingCase;
import com.asset.appwork.model.IncomingRegistration;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.MemoValuesRepository;
import com.asset.appwork.repository.MemosRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
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

    public File memoToDocx(Memos memo, User user, IncomingRegistration incomingRegistration, IncomingCase incomingCase) throws AppworkException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
        Date date = new Date();

        JsonNode jsonNode;
        try {
            jsonNode = SystemUtil.convertStringToJsonNode(SystemUtil.readFile(System.getProperty("user.dir") + env.getProperty("memosPath") + memo.getJsonId() + ".json"));
            jsonNode = jsonNode.get("app").get("pages").get("page").get(0).get("sections").get("sec").get(0).get("forms");
            List<String> sections = new ArrayList<>();
            for (int i = 0; i < jsonNode.size(); i++) {
                sections.add(jsonNode.get(i).get("name").asText());
            }

            HashMap<String, String> memoValues = memo.getValues();
            Object[] memoValuesObjectArray = memoValues.values().toArray();
            String[] memoValuesStringArray = Arrays.copyOf(memoValuesObjectArray, memoValuesObjectArray.length, String[].class);
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < memoValuesStringArray.length; i++) {
                if (!sections.get(i).equals("Header")) {
                    value.append("<p style = 'font-size: 27px; font-weight: bold; text-align:right;'><u>").append(sections.get(i)).append("</u></p>");
                }
                value.append(memoValuesStringArray[i]);
            }

            Document doc = Jsoup.parse(value.toString());

            Optional<Group> group = user.getGroup().stream().findFirst();
            String groupCode = "";
            if (group.isPresent()) {
                groupCode = group.get().getGroupCode();
            }

            String signature = "https://i.ibb.co/jTMsXYg/signature.png";
            String name = user.getPerson().getTitle() + "/" + user.getDisplayName();
            try {
                if (!doc.select("#" + groupCode + "signatureParagraph").isEmpty()) {
                    doc.select("#" + groupCode + "signatureParagraph").first();
                    doc.select("#" + groupCode + "signatureImage").first().attr("src", signature);
                    doc.select("#" + groupCode + "nameParagraph").first().text(name);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                log.error("Docx: " + e.getMessage());
                throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
            }

            try {
                if (!doc.select("#Agency").isEmpty()) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Egypt/Cairo"));
                    calendar.setTime(incomingRegistration.getIncomingDate());
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    String dateFormat = day + "/" + month + "/" + year;
                    doc.select("#Agency").first().text(incomingRegistration.getResponsibleEntityGehazTxt());
                    doc.select("#Sector").first().text(incomingRegistration.getResponsibleEntityKeta3Txt());
                    doc.select("#Office").first().text(incomingRegistration.getResponsibleEntityEdaraTxt());
                    doc.select("#Constraint").first().text("القيـــــــد" + ":" + incomingRegistration.getIncomingNumber());
                    doc.select("#Date").first().text(" " + dateFormat);
                    doc.select("#caseNumber").first().text("من القضية رقم" + " " + incomingCase.getCaseNumber());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                log.error("Docx: " + e.getMessage());
                throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
            }
            value = new StringBuilder(doc.toString());

            if (value.toString().contains("<img")) {
                int imageIndex = value.indexOf("<img");
                int imageEndIndex = value.indexOf(">", imageIndex) + 1;
                value.insert(imageEndIndex, "</img>");
            }
            if (value.toString().contains("<link")) {
                int linkIndex = value.indexOf("<link");
                int linkEndIndex = value.indexOf(">", linkIndex) + 1;
                value.insert(linkEndIndex, "</link>");
            }

            String stringValue = value.toString();
            stringValue = stringValue.replaceAll("<br>", "<br></br>");
            stringValue = stringValue.replaceAll("<p> </p>", "");
            stringValue = stringValue.replaceAll("(?m)^[ \t]*\r?\n", "");
            System.out.println(stringValue);

            POIFSFileSystem poifsFileSystem = new POIFSFileSystem();
            DirectoryEntry directoryEntry = poifsFileSystem.getRoot();

            if (!Files.isDirectory(Paths.get("Temp"))) {
                File theDir = new File("Temp");
                theDir.mkdirs();
            }

            OutputStream outputStream = new FileOutputStream("Temp" + File.separator + "Memo" + memo.getRequestId() + " " + simpleDateFormat.format(date) + ".doc");
            try {
                InputStream inputStream = new ByteArrayInputStream(stringValue.getBytes());
                directoryEntry.createDocument("WordDocument", inputStream);
                poifsFileSystem.writeFilesystem(outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Docx: " + e.getMessage());
        }
        return new File(System.getProperty("user.dir") + File.separator + "Temp" + File.separator + "Memo" + memo.getRequestId() + " " + simpleDateFormat.format(date) + ".doc");
    }
}
