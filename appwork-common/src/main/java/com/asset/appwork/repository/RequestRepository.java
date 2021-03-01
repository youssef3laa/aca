package com.asset.appwork.repository;

import com.asset.appwork.model.RequestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends GenericRepository<RequestEntity, Long> {

    @Query("select count(r.requestDate) from RequestEntity r where year(r.requestDate) = :year and ( r.status !='created' and r.status != 'draft' )")
    Long countByRequestDate(Integer year);
    //
    // get by specific request status and get older date  from source incoming only
    //    @Query("select r from RequestEntity r where r.process = :process and r.date < :requestDate and(r.subject like %:subject% or r.requestNumber like %:requestNumber%)")
    @Query("select r from RequestEntity r where r.process = :process and(r.subject like %:subject% and r.requestNumber like %:requestNumber%)")
    List<RequestEntity> getRequestsByProcessAndRequestDateAndSubjectAndRequestNumber(@Param("process") String process,
                                                                              @Param("subject") String subject,
                                                                              @Param("requestNumber") String requestNumber);
}
