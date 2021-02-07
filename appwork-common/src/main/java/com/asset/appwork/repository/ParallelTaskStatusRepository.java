package com.asset.appwork.repository;

import com.asset.appwork.model.ParallelTaskStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface ParallelTaskStatusRepository extends GenericRepository<ParallelTaskStatus, Long> {
    Integer countDistinctByRequestIdAndOwnerAndStatus(String requestId, String owner, Integer status);
}
