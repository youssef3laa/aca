package com.asset.appwork.repository;

import com.asset.appwork.model.ApprovalHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Bassel on 29/12/2020.
 */
@Repository
public interface ApprovalHistoryRepository extends PagingAndSortingRepository<ApprovalHistory, Long> {
    Integer countByProcessNameAndEntityId(String processName, String entityId);

    List<ApprovalHistory> findByProcessNameAndEntityId(String processName, String entityId);
    List<ApprovalHistory> findByProcessNameAndEntityId(String processName, String entityId, Pageable pageable);

    Optional<ApprovalHistory> findTop1ByProcessNameAndEntityIdOrderByIdDesc(String processName, String entityId);
}