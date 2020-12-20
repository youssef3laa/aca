package com.asset.appwork.platform.soap;

public class Process {

    public String initiate(String ticket, String processName, String params) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <"+processName+" xmlns=\"http://schemas.cordys.com/default\">\n" +
                        params +"\n"+
                "    </"+processName+">" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
