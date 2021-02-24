package com.asset.appwork.service;

import com.asset.appwork.AppBuilder;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.User;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AppBuilderService {
    @Autowired
    OrgChartService orgChartService;
    @Autowired
    Environment environment;
    @Autowired
    SecurityService securityService;

    public void createPage(String fileName, Account account) throws AppworkException {
        fileName = fileName.split("\\.")[0];
        AppBuilder appBuilder = new AppBuilder(environment.getProperty("form.config"));
        StringBuilder file = appBuilder.retrieveFile(fileName);
        String updatedFile = updateAccess(file, account);
        try {
            appBuilder.writeFile(fileName,updatedFile);
        } catch (IOException e){
            throw new AppworkException(ResponseCode.CREATE_FILE_FAILURE);
        }
    }

    private String updateAccess(StringBuilder file, Account account) throws AppworkException{
        User user = orgChartService.getLoggedInUser(account);
        String newFile = file.toString();
        try {
            HashMap fileMap = new HashMap();
            fileMap.put("app", (HashMap) SystemUtil.readJSONObject(file.toString(),"app", HashMap.class));
            traverseFile(fileMap, user);
            newFile = SystemUtil.writeObjectIntoString(fileMap);
        }catch (JsonProcessingException e){
            return newFile;
        }
        return newFile;
    }

    private void traverseFile(HashMap fileMap, User user){
        List<Object> keys = new ArrayList<>();
        fileMap.forEach((key, value) -> {
            if(value instanceof HashMap){
                if(((HashMap)value).containsKey("canAccess")){
                    if(((HashMap)value).get("canAccess") instanceof String
                        && !securityService.canAccess( (String) ((HashMap)value).get("canAccess"), user)) {
                        keys.add(key);
                        ((HashMap)value).remove("canAccess");
                        return;
                    }
                    ((HashMap)value).remove("canAccess");
                }
                traverseFile((HashMap) value, user);
            }else if(value instanceof ArrayList){
                List<Integer> indexes = new ArrayList<>();
                List valueList = (ArrayList)value;
                valueList.stream().forEach(l->{
                  if(l instanceof HashMap){
                      if(((HashMap)l).containsKey("canAccess")){
                          if (((HashMap)l).get("canAccess") instanceof String && !securityService.canAccess( (String) ((HashMap)l).get("canAccess"), user)) {
                              ((HashMap)l).remove("canAccess");
                              indexes.add((valueList).indexOf(l));
                              return;
                          }
                          ((HashMap)l).remove("canAccess");
                      }
                      traverseFile((HashMap) l, user);
                  }
                });
                indexes.forEach(index -> ((ArrayList)value).remove((int)index));
            }
        });
        keys.forEach(fileMap::remove);
    }
}
