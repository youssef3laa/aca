package com.asset.appwork.repository;

import com.asset.appwork.model.Outcoming;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OutcomingRepository extends GenericRepository<Outcoming,Long> {
    @Query("select count(o.outcomingDate) from Outcoming o where year(o.outcomingDate) = :year")
    Long countByOutcomingDate(Integer year);

}
