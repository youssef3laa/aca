package com.asset.appwork.repository;

import com.asset.appwork.model.Outcoming;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OutcomingRepository extends GenericRepository<Outcoming,Long> {

    Long countByOutcomingDate(Date date);
}
