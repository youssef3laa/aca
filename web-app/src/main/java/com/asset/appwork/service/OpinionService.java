package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.Opinion;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.OpinionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OpinionService {

    @Autowired
    OpinionRepository opinionRepository;
    @Autowired
    OrgChartService orgChartService;

    public Page<Opinion> findByRequestId(String requestId, Pageable pageable){
        Page<Opinion> opinions = opinionRepository.findByRequestIdOrderByOpinionDateDesc(requestId, pageable);
        addUserToOpinions(opinions.getContent());
        return opinions;
    }

    @Transactional
    public void addUserToOpinions(List<Opinion> opinions) {
        opinions.stream().forEach(opinion -> {
            try {
                if(opinion.getUserCN().contains("users")){
                    User user = orgChartService.getUserByUserId(opinion.getUserCN().split(",")[0].replace("cn=",""));
                    Optional<Group> group = user.getGroup().stream().findFirst();
                    if(group.isPresent()){
                        opinion.setDisplayName(group.get().getNameAr());
                        opinion.setUnitName(group.get().getUnit().getNameAr());
                    }else{
                        throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
                    }
                }else{
                    Group group = orgChartService.getGroupByCn(opinion.getUserCN());
                    opinion.setDisplayName(group.getNameAr());
                    opinion.setUnitName(group.getUnit().getNameAr());
                }
            }catch (AppworkException e){
                throw new RuntimeException(e);
            }
        });
    }

}
