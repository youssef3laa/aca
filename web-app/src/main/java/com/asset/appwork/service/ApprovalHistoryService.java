package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.ApprovalHistory;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.ApprovalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalHistoryService {
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    OrgChartService orgChartService;

    @Transactional
    public List<ApprovalHistory> addDisplayNameToApprovals(List<ApprovalHistory> approvals) {
        approvals.forEach(approvalHistory -> {
            try {
                if (approvalHistory.getUserCN() != null) {
                    if (approvalHistory.getUserCN().contains("users")) {
                        User user = orgChartService.getUserByUserId(approvalHistory.getUserCN().split(",")[0].replace("cn=", ""));
                        Optional<Group> group = user.getGroup().stream().findFirst();
                        if (group.isPresent()) {
                            approvalHistory.setDisplayName(group.get().getNameAr());
                            approvalHistory.setUnitName(group.get().getUnit().getNameAr());
                        } else {
                            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        Group group = orgChartService.getGroupByCn(approvalHistory.getUserCN());
                        approvalHistory.setDisplayName(group.getNameAr());
                        approvalHistory.setUnitName(group.getUnit().getNameAr());
                    }
                }
                if (approvalHistory.getReceiverCN() != null) {
                    if (approvalHistory.getReceiverCN().contains("users")) {
                        User user = orgChartService.getUserByUserId(approvalHistory.getReceiverCN().split(",")[0].replace("cn=", ""));
                        Optional<Group> group = user.getGroup().stream().findFirst();
                        if (group.isPresent()) {
                            approvalHistory.setReceiverDisplayName(group.get().getNameAr());
                        } else {
                            throw new AppworkException(ResponseCode.INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        Group group = orgChartService.getGroupByCn(approvalHistory.getReceiverCN());
                        approvalHistory.setReceiverDisplayName(group.getNameAr());
                    }
                }
            } catch (AppworkException e) {
                throw new RuntimeException(e);
            }
        });
        return approvals;
    }

    public void updateReceiveDate(Long id) throws AppworkException {
        Optional<ApprovalHistory> approvalHistory = approvalHistoryRepository.findTopByRequestIdOrderByIdDesc(id);
        if (approvalHistory.isPresent()) {
            approvalHistory.get().setReceiveDate(new Date());
            approvalHistoryRepository.save(approvalHistory.get());
        } else {
            throw new AppworkException(ResponseCode.NO_DATA_SAVED);
        }
    }
}