package com.asset.appwork.platform.soap;

public class Entity {
    public String readEntity(String entityName, String entityId) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <Read" + entityName + " xmlns=\"http://schemas/AssetGeneralACA/" + entityName + "/operations\">\n" +
                "      <ns0:" + entityName + "-id xmlns:ns0=\"http://schemas/AssetGeneralACA/" + entityName + "\">\n" +
                "        <ns0:Id>" + entityId + "</ns0:Id>\n" +
                "      </ns0:" + entityName + "-id>\n" +
                "    </Read" + entityName + ">\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String create(String ticket, String projectName, String entityName, String params) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>" + ticket + "</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <Create" + entityName + " xmlns=\"http://schemas/" + projectName + "/" + entityName + "/operations\">\n" +
                "      <" + entityName + "-create xmlns:=\"http://schemas/" + projectName + "/" + entityName + "\">\n" +
                params + "\n" +
                "      </" + entityName + "-create>\n" +
                "    </Create" + entityName + ">" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
