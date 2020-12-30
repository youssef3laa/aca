package com.asset.appwork.repository;

import com.asset.appwork.model.ApprovalHistory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Bassel on 29/12/2020.
 */
@Repository
public interface ApprovalHistoryRepository extends GenericRepository<ApprovalHistory, Long> {
    Optional<List<ApprovalHistory>> findByProcessNameAndEntityId(String processName, String entityId);

    Optional<ApprovalHistory> findTop1ByProcessNameAndEntityIdOrderByIdDesc(String processName, String entityId);
}