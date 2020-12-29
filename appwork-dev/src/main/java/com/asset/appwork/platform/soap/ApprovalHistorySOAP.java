package com.asset.appwork.platform.soap;

import java.util.Date;

public class ApprovalHistorySOAP {
//    public String getHistoryByPid( String processId){
//        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
//
//                "  <SOAP:Body>\n" +
//                "    <ACA_Find_webService_get_approvals xmlns=\"http://schemas/AssetGeneralACA/ACA_Entity_approval_history/operations\">\n" +
//                "      <processId>"+processId+"</processId>\n" +
//                "      <ns0:Cursor xmlns:ns0=\"http://schemas.opentext.com/bps/entity/core\" offset=\"0\" limit=\"100\" />\n" +
//                "    </ACA_Find_webService_get_approvals>\n" +
//                "  </SOAP:Body>\n" +
//                "</SOAP:Envelope>";
//    }

    public String createHistoryApproval( ApprovalHistory approvalHistory){

        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +

                "  <SOAP:Body>\n" +
                "    <CreateACA_Entity_approval_history xmlns=\"http://schemas/AssetGeneralACA/ACA_Entity_approval_history/operations\">\n" +
                "      <ns0:ACA_Entity_approval_history-create xmlns:ns0=\"http://schemas/AssetGeneralACA/ACA_Entity_approval_history\">\n" +
                "        <ns0:decision>"+approvalHistory.decision+"</ns0:decision>\n" +
                "        <ns0:comment>"+approvalHistory.comment+"</ns0:comment>\n" +
                "        <ns0:roleName>"+approvalHistory.roleName+"</ns0:roleName>\n" +
                "        <ns0:userName>"+approvalHistory.userName+"</ns0:userName>\n" +
                "        <ns0:processId>"+approvalHistory.processId+"</ns0:processId>\n" +
                "      </ns0:ACA_Entity_approval_history-create>\n" +
                "    </CreateACA_Entity_approval_history>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
