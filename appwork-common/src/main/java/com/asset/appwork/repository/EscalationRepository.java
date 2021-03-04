package com.asset.appwork.repository;

import com.asset.appwork.model.Escalation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscalationRepository extends GenericRepository<Escalation, Long> {
    Optional<Escalation> findById(Long id);

    Optional<Escalation> findByJobType(Integer jobType);

    List<Escalation> findAll();
    Page<Escalation> findAll(Pageable pageable);

    void deleteById(Long id);
}
