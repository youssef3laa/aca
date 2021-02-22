package com.asset.appwork.service;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Security;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    SecurityRepository securityRepository;
    @Autowired
    OrgChartService orgChartService;

    public boolean canAccess(String target, User user) {
        Optional<Group> group = user.getGroup().stream().findFirst();
        Unit unit;
        if(group.isPresent()){
            unit = group.get().getUnit();
        }else{
            return false;
        }
        Optional<Security> security = securityRepository.findByTarget(target);
        if(security.isPresent()){
            if(security.get().getType().equals(1)){
                if(security.get().getConfig().contains(";"+unit.getUnitTypeCode()+";")){
                    return true;
                }
            }else if(security.get().getType().equals(2)){
                if(security.get().getConfig().contains(";"+unit.getUnitCode()+";")){
                    return true;
                }
            }else if(security.get().getType().equals(3)){
                if(security.get().getConfig().contains(";"+group.get().getGroupCode()+";")){
                    return true;
                }
            }
        }
        return false;
    }

}
