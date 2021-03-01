package com.asset.appwork.repository;

import com.asset.appwork.model.ApprovalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Bassel on 29/12/2020.
 */
@Repository
public interface ApprovalHistoryRepository extends GenericRepository<ApprovalHistory, Long> {

    //    List<ApprovalHistory> findByProcessNameAndEntityId(String processName, String entityId);
    Page<ApprovalHistory> findByRequestIdOrderByApprovalDateAsc(Long requestId, Pageable pageable);

    Page<ApprovalHistory> findByProcessNameAndEntityIdOrderByApprovalDateAsc(String processName, String entityId, Pageable pageable);


    Optional<ApprovalHistory> findTopByRequestIdOrderByIdDesc(Long requestId);

    Page<ApprovalHistory> findByUserCNOrUserCNOrderByApprovalDateAsc(String userCN, String roleCN, Pageable pageable);
    Long countByUserCNOrUserCN(String userCN, String roleCN);
}