package com.asset.appwork.repository;

import com.asset.appwork.model.Opinion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionRepository extends GenericRepository<Opinion, Long> {

    Page<Opinion> findByRequestIdOrderByOpinionDateDesc(String requestId, Pageable pageable);

}
