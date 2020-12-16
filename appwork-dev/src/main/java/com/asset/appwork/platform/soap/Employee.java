package com.asset.appwork.platform.soap;

/**
 * Created by omar.sabry on 11/11/2020.
 */
public class Employee {
    public String initiateEmployeeApproval(String ticket, String Fname, String Lname, String email){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>"+ticket+"</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <ACA_BP_Emp_employeeData_approval xmlns=\"http://schemas.cordys.com/default\">\n" +
                "      <Fname>"+Fname+"</Fname>\n" +
                "      <Lname>"+Lname+"</Lname>\n" +
                "      <email>"+email+"</email>\n" +
                "    </ACA_BP_Emp_employeeData_approval>" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }
}
