package com.asset.appwork.platform.soap;


public class Notifications {
    public String readNotification (String notificationId){
     return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
             "  <SOAP:Body>\n" +
             "    <GetNotification xmlns=\"http://schemas.cordys.com/notification/workflow/1.0\">\n" +
             "      <NotificationId>"+notificationId+"</NotificationId>\n" +
             "    </GetNotification>\n" +
             "  </SOAP:Body>\n" +
             "</SOAP:Envelope>";
    }
    public String getNotifications(){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "<SOAP:Body>\n" +
                "<GetNotifications xmlns=\"http://schemas.cordys.com/notification/workflow/1.0\" countOnly=\"false\">\n" +
                "<ns0:Query xmlns:ns0=\"http://schemas.cordys.com/cql/1.0\">\n" +
                "<ns0:Select >\n" +
                "<ns0:QueryableObject>Notification</ns0:QueryableObject>\n"+
                "<ns0:Field>NotificationId</ns0:Field>\n" +
                "<ns0:Field>MessageId</ns0:Field>\n" +
                "<ns0:Field>Sender</ns0:Field>\n" +
                "<ns0:Field>Receiver</ns0:Field>\n"+
                "<ns0:Field>Subject</ns0:Field>\n"+
                "<ns0:Field>Source</ns0:Field>\n" +
                "<ns0:Field>State</ns0:Field>\n" +
                "<ns0:Field>DeliveryDate</ns0:Field>\n" +
                "</ns0:Select>\n"+
                "<ns0:Filters>\n" +
                "<EQ field=\"RETURN_NOTIFICATION_DATA\">\n" +
                "<Value>true</Value></EQ>\n" +
                "</ns0:Filters>\n"+
                "</ns0:Query>\n"+
                "</GetNotifications>\n"+
                "</SOAP:Body>\n"+
                "</SOAP:Envelope>\n";
    }
}