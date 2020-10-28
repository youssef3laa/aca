package com.asset.appwork.cordys;

import com.asset.appwork.service.CordysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karim on 10/26/20.
 */
@Data
@Service
public class CordysManagement {
    static ConcurrentHashMap<String, Cordys> concurrentHashMap = new ConcurrentHashMap();
    @Autowired
    CordysService cordysService;

    public CordysManagement(){
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                checkInActiveTenant();
            }
        });
        timer.start();
    }

    public User create(String username, String password, String organization) throws JsonProcessingException {
        Cordys cordys = this.get(organization);
        User user = cordys.create(username, password);
        return user;
    }

    public Cordys get(String organization){
        Cordys cordys = concurrentHashMap.get(organization);
        Optional.of(cordys).orElseGet(()->{
            Cordys c = new Cordys(cordysService, organization);
            concurrentHashMap.put(organization, c);
            return c;
        });
        updateLastActiveTime(cordys);
        return cordys;
    }

    private void updateLastActiveTime(Cordys cordys){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 15);
        cordys.setLastActiveTime(calendar.getTime());
    }

    private void checkInActiveTenant(){
        for (String key : concurrentHashMap.keySet()) {
            Cordys cordys = concurrentHashMap.get(key);
            Date now = new Date();
            if (now.compareTo(cordys.getLastActiveTime()) > 0) {
                System.out.println( "********************* " +key+ " in active organization will be deleted *********************");
                concurrentHashMap.remove(key);
            }
        }
    }



    @Data
    public class Cordys {

        String domain;
        String url = "";
        String organization;
        Date lastActiveTime;
        CordysService cordysService;
        ConcurrentHashMap<String, User> concurrentHashMap = new ConcurrentHashMap();


        public Cordys(){
            Timer timer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {

                    checkInActiveUser();
                }
            });
            timer.start();
        }

        public Cordys(CordysService cordysService, String organization){
            super();
            this.cordysService = cordysService;
            this.organization = organization;
            this.url ="http://appworks-dev:81/home/"+organization+"/com.eibus.web.soap.Gateway.wcp";
        }

        private void checkInActiveUser(){
            for (String key : concurrentHashMap.keySet()) {
                User user= concurrentHashMap.get(key);
                Date now = new Date();
                if (now.compareTo(user.getLastActiveTime()) > 0) {
                    System.out.println( "********************* " +key+ " in active user will be deleted *********************");
                    concurrentHashMap.remove(key);
                }
            }
        }

        public User get(String id){
            return this.concurrentHashMap.get(id);
        }

        public String getSAMLart(String id){
            return this.concurrentHashMap.get(id).getSAMLart();
        }

        public User create(String username, String password) throws JsonProcessingException {

            String SAMLart = cordysService.login(username, password);
            User user = new User(SAMLart, url);
            concurrentHashMap.put(SAMLart, user);
            return user;
        }

        private void updateLastActiveTime(User user){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 15);
            user.setLastActiveTime(calendar.getTime());
        }

        private String generateUUID() throws UnsupportedEncodingException, NoSuchAlgorithmException {
            MessageDigest salt = MessageDigest.getInstance("SHA-256");
            salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
            return bytesToHex(salt.digest());
        }

        public String bytesToHex(byte[] bytes) {
            StringBuilder result = new StringBuilder();
            for (byte aByte : bytes) {
                result.append(String.format("%02x", aByte));
            }
            return result.toString();
        }


    }

    @Data
    public class User {
        String SAMLart;
        String link;
        Date lastActiveTime;
        String url;
        com.asset.appwork.model.User user;

        User(String SAMLart, String url){
            this.SAMLart = SAMLart;
            this.url = url +"?SAMLart="+this.SAMLart;
        }
    }
}
