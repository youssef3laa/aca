package com.asset.appwork.platform.soap;

import com.asset.appwork.model.ApprovalHistory;

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
                "        <ns0:decision>"+approvalHistory.getDecision()+"</ns0:decision>\n" +
                "        <ns0:comment>"+approvalHistory.getComment()+"</ns0:comment>\n" +
                "        <ns0:stepId>"+approvalHistory.getStepId()+"</ns0:stepId>\n" +
                "        <ns0:userCN>"+approvalHistory.getUserCN()+"</ns0:userCN>\n" +
                "        <ns0:entityId>"+approvalHistory.getEntityId()+"</ns0:entityId>\n" +
                "        <ns0:processName>"+approvalHistory.getProcessName()+"</ns0:processName>\n" +
                "      </ns0:ACA_Entity_approval_history-create>\n" +
                "    </CreateACA_Entity_approval_history>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
