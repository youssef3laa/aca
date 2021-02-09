package com.asset.appwork.repository;

import com.asset.appwork.model.RequestEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RequestRepository extends GenericRepository<RequestEntity, Long> {
    Long countDistinctByDateAfter(Date date);
}
