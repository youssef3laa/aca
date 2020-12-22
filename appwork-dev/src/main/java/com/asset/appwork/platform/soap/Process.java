package com.asset.appwork.platform.soap;

public class Process {

    public String initiate(String params) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <ACA_BP_processRouting xmlns=\"http://schemas.cordys.com/default\">\n" +
                        params +"\n"+
                "    </ACA_BP_processRouting>" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
