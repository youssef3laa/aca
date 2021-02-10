package com.asset.appwork.platform.soap;

import com.asset.appwork.dto.Memos;

/**
 * Created by Bassel on 3/1/2021.
 */
public class memorandumSOAP {

    public String createMemorandum(Memos memos){

        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <CreateACA_Entity_Memos xmlns=\"http://schemas/AssetGeneralACA/ACA_Entity_Memos/operations\">\n" +
                "      <ns0:ACA_Entity_Memos-create xmlns:ns0=\"http://schemas/AssetGeneralACA/ACA_Entity_Memos\">\n" +
                "        <ns0:requestId>"+memos.getRequestId()+"</ns0:requestId>\n" +
                "        <ns0:jsonId>"+memos.getJsonId()+"</ns0:jsonId>\n" +
                "      <ns0:nodeId>"+memos.getNodeId()+"</ns0:nodeId>"+
                "      </ns0:ACA_Entity_Memos-create>\n" +
                "    </CreateACA_Entity_Memos>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String   createMemoValues(Memos memos, long id){
         String notes = "";
         for(String key: memos.getValues().keySet()) {
             notes += "<ns0:ACA_Entity_MemosValues-create xmlns:ns0=\"http://schemas/AssetGeneralACA/ACA_Entity_MemosValues\">\n" +
                     "  <ns0:memosId>" + id + "</ns0:memosId>\n" +
                     "  <ns0:jsonKey>" + key + "</ns0:jsonKey>\n" +
                     "  <ns0:value>"+ memos.getValues().get(key) +"</ns0:value>\n" +
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
