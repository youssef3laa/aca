package com.asset.appwork.repository;

import com.asset.appwork.model.RequestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestRepository extends GenericRepository<RequestEntity, Long> {
    Long countDistinctByDateAfter(Date date);


    // get by specific request status and get older date only
    //    @Query("select r from RequestEntity r where r.process = :process and r.date < :requestDate and(r.subject like %:subject% or r.requestNumber like %:requestNumber%)")
    @Query("select r from RequestEntity r where r.process = :process and(r.subject like %:subject% and r.requestNumber like %:requestNumber%)")
    List<RequestEntity> getRequestsByProcessAndDateAndSubjectAndRequestNumber(@Param("process") String process,
                                                                              @Param("subject") String subject,
                                                                              @Param("requestNumber") String requestNumber);
}
