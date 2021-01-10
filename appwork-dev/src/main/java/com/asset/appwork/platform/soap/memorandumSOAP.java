package com.asset.appwork.platform.soap;

import com.asset.appwork.dto.Memos;
import com.asset.appwork.model.memoValues;

/**
 * Created by Bassel on 3/1/2020.
 */
public class memorandumSOAP {

    public String createMemorandum(Memos memos){

        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <CreateACA_Entity_Memos xmlns=\"http://schemas/AssetGeneralACA/ACA_Entity_Memos/operations\">\n" +
                "      <ns0:ACA_Entity_Memos-create xmlns:ns0=\"http://schemas/AssetGeneralACA/ACA_Entity_Memos\">\n" +
                "        <ns0:requestId>"+memos.getRequestId()+"</ns0:requestId>\n" +
                "        <ns0:jsonId>"+memos.getJsonId()+"</ns0:jsonId>\n" +
                "      </ns0:ACA_Entity_Memos-create>\n" +
                "    </CreateACA_Entity_Memos>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String createMemoValues(Memos memos){
         String notes = "";
         for(String value: memos.getNotes()) {
             notes += "<ns0:ACA_Entity_MemosValues-create xmlns:ns0=\"http://schemas/AssetGeneralACA/ACA_Entity_MemosValues\">\n" +
                     "  <ns0:memosId>" + 2 + "</ns0:memosId>\n" +
                     "  <ns0:jsonKey>" + memos.getKey()+ "</ns0:jsonKey>\n" +
                     "  <ns0:value>"+ value +"</ns0:value>\n" +
                     "</ns0:ACA_Entity_MemosValues-create>\n";
         }

        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <CreateACA_Entity_MemosValues xmlns=\"http://schemas/AssetGeneralACA/ACA_Entity_MemosValues/operations\">\n" +
                    notes +
                "    </CreateACA_Entity_MemosValues>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}