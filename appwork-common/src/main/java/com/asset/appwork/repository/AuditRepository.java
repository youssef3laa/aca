package com.asset.appwork.repository;

import com.asset.appwork.model.Audit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AuditRepository extends GenericRepository<Audit,Long> {

    Page<Audit> findAll(Pageable pageable);

    @Query("SELECT audit FROM Audit audit  WHERE lower(audit.userCN) LIKE %:search% OR lower(audit.takenAction) LIKE %:search% OR lower(audit.responseCode) LIKE %:search%  order by audit.Id desc")
    Page<Audit> searchAudit(String search, Pageable pageable);
}
