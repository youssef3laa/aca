package com.asset.appwork.webservice;

public class Entity {
    public String readEntity(String ticket, String projectName, String entityName, String entityId) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>" + ticket + "</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <Read" + entityName + " xmlns=\"http://schemas/" + projectName + "/" + entityName + "/operations\">\n" +
                "      <ns0:" + entityName + "-id xmlns:ns0=\"http://schemas/" + projectName + "/" + entityName + "\">\n" +
                "        <ns0:Id>" + entityId + "</ns0:Id>\n" +
                "      </ns0:" + entityName + "-id>\n" +
                "    </Read" + entityName + ">\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }


}
