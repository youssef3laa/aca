package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalHistoryService {

    @Autowired
    OrgChartService orgChartService;

    @Transactional
    public List<ApprovalHistory> addDisplayNameToApprovals(List<ApprovalHistory> approvals) throws AppworkException {
        approvals.stream().forEach(approvalHistory -> {
            try {
                if(approvalHistory.getUserCN().contains("users")){
                        User user = orgChartService.getUserByUserId(approvalHistory.getUserCN().split(",")[0].replace("cn=",""));
                        Optional<Group> group = user.getGroup().stream().findFirst();
                        if(group.isPresent()){
                            approvalHistory.setDisplayName(group.get().getName_ar());
                            approvalHistory.setUnitName(group.get().getUnit().getName_ar());
                        }else{
                            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
                        }
                }else{
                        Group group = orgChartService.getGroupByCn(approvalHistory.getUserCN());
                        approvalHistory.setDisplayName(group.getName_ar());
                        approvalHistory.setUnitName(group.getUnit().getName_ar());
                }
            }catch (AppworkException e){
                throw new RuntimeException(e);
            }
        });
        return approvals;
    }
}