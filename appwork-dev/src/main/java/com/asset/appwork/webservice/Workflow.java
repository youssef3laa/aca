package com.asset.appwork.webservice;

/**
 * Created by karim on 11/3/20.
 */
public class Workflow {
    public String getHumanTasks(String ticket){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>"+ticket+"</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <GetHumanTasks xmlns=\"http://schemas.cordys.com/notification/workflow/1.0\" countOnly=\"false\">\n" +
                "      <ns0:Query xmlns:ns0=\"http://schemas.cordys.com/cql/1.0\">\n" +
                "        <ns0:Select distinct=\"true\">\n" +
                "          <ns0:QueryableObject>TASK_INSTANCE</ns0:QueryableObject>\n" +
                "          <ns0:Field>TaskId</ns0:Field>\n" +
                "          <ns0:Field>SourceInstanceId</ns0:Field>\n" +
                "          <ns0:Field>State</ns0:Field>\n" +
                "          <ns0:Field>ProcessName</ns0:Field>\n" +
                "          <ns0:Field>Activity</ns0:Field>\n" +
                "          <ns0:Field>Priority</ns0:Field>\n" +
                "          <ns0:Field>Target</ns0:Field>\n" +
                "          <ns0:Field>Sender</ns0:Field>\n" +
                "          <ns0:Field>SourceType</ns0:Field>\n" +
                "          <ns0:Field>Assignee</ns0:Field>\n" +
                "          <ns0:Field>DeliveryDate</ns0:Field>\n" +
                "          <ns0:Field>StartDate</ns0:Field>\n" +
                "          <ns0:Field>DueDate</ns0:Field>\n" +
                "          <ns0:Field>UITaskId</ns0:Field>\n" +
                "          <ns0:Field>State</ns0:Field>\n" +
                "        </ns0:Select>\n" +
                "        <ns0:Filters>\n" +
                "                <EQ field=\"RETURN_TASK_DATA\">\n" +
                "                    <Value>true</Value>\n" +
                "                </EQ>\n" +
                "        </ns0:Filters>\n" +
                "      </ns0:Query>\n" +
                "    </GetHumanTasks>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

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

    public String claimTask(String ticket, String taskId){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>" + ticket + "</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <ClaimTask xmlns=\"http://schemas.cordys.com/notification/workflow/1.0\">\n" +
                "      <TaskId>"+taskId+"</TaskId>\n" +
                "    </ClaimTask>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String getTask(String ticket, String taskId) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>" + ticket + "</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <GetTask xmlns=\"http://schemas.cordys.com/notification/workflow/1.0\">\n" +
                "      <TaskId>"+taskId+"</TaskId>\n" +
                "      <RetrievePossibleActions>true</RetrievePossibleActions>\n" +
                "      <ReturnTaskData>true</ReturnTaskData>\n" +
                "    </GetTask>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
