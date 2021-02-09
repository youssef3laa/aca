package com.asset.appwork.soup;

public class ProcessSOAP {

    public String pauseProcessMsg(String processInstanceId) {

        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <SOAP:Body>\n" +
                "        <SuspendProcess xmlns=\"http://schemas.cordys.com/bpm/monitoring/1.0\">\n" +
//                "            <processname>PARAMETER</processname>\n" +
                "            <receiver>" + processInstanceId + "</receiver>\n" +
                "            <batch>true</batch>\n" +
                "        </SuspendProcess>\n" +
                "    </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String resumeProcessMsg(String processInstanceId) {

        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <SOAP:Body>\n" +
                "        <ResumeProcess xmlns=\"http://schemas.cordys.com/bpm/monitoring/1.0\">\n" +
                "            <receiver>" + processInstanceId + "</receiver>\n" +
                "            <batch>true</batch>\n" +
                "        </ResumeProcess>\n" +
                "    </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
