package com.asset.appwork;

import com.asset.appwork.util.Http;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AppworkCSApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppworkCSApplication.class, args);
//        Http http = new Http();

//        String data = "{\"where\" : \"" + "المشورة" + "\" }";
        Http http = new Http().setDoAuthentication(true)
                .setData(new StringPart[]{
                        new StringPart("where", "المشورة")
                })
                .basicAuthentication("admin", "Asset99a")
                .setContentType(Http.ContentType.FORM_REQUEST)
                .post("http://cs/otcs/cs.exe/api/v2/search");
        System.out.println(http.getResponse());

    }
}
