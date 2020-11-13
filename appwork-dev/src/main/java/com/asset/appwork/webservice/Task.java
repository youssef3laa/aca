package com.asset.appwork.webservice;

/**
 * Created by omar.sabry on 11/12/2020.
 */
public class Task {
    public String performTaskAction(String ticket, String taskId, String action, String memo, String data) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>" + ticket + "</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <PerformTaskAction xmlns=\"http://schemas.cordys.com/notification/workflow/1.0\">\n" +
                "      <TaskId>"+taskId+"</TaskId>\n" +
                "      <Action>"+action+"</Action>\n" +
                "      <Memo>"+memo+"</Memo>\n" +
                "      <Data>"+data+"</Data>\n" +
                "    </PerformTaskAction>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
