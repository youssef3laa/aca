package com.asset.appwork.repository;

import com.asset.appwork.model.Escalation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscalationRepository extends GenericRepository<Escalation, Long> {
    Optional<Escalation> findById(Long id);

    void deleteById(Long id);
}
