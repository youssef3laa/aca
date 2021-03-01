package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    OrgChartService orgChartService;

    public Optional<Unit> getUnitByCN(String cn) throws AppworkException{
        Optional<Group> group = getGroupByCN(cn);
        if(group.isPresent()){
            return Optional.ofNullable(group.get().getUnit());
        }
        return Optional.empty();
    }

    public Optional<Group> getGroupByCN(String cn) throws AppworkException {
        if(cn.contains("users")){
            User user = orgChartService.getUserByUserId(cn.split(",")[0].replace("cn=", ""));
            Optional<Group> group = user.getGroup().stream().findFirst();
            return group;
        }else{
            Group group = orgChartService.getGroupByCn(cn);
            return Optional.ofNullable(group);
        }
    }

}
