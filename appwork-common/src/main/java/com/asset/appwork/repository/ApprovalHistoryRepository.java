package com.asset.appwork.repository;

import com.asset.appwork.model.ApprovalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Created by Bassel on 29/12/2020.
 */
@Repository
public interface ApprovalHistoryRepository extends GenericRepository<ApprovalHistory, Long> {

//    List<ApprovalHistory> findByProcessNameAndEntityId(String processName, String entityId);
    Page<ApprovalHistory> findByRequestIdOrderByApprovalDateDesc(String requestId, Pageable pageable);

    Page<ApprovalHistory> findByProcessNameAndEntityIdOrderByApprovalDateDesc(String processName, String entityId, Pageable pageable);

//    Optional<ApprovalHistory> findTop1ByProcessNameAndEntityIdOrderByIdDesc(String processName, String entityId);

    Page<ApprovalHistory> findByUserCNOrderByApprovalDateDesc(String userCN,Pageable pageable);
}