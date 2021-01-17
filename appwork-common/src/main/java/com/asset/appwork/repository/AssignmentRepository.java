package com.asset.appwork.repository;

import com.asset.appwork.model.Assignment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssignmentRepository extends GenericRepository<Assignment, Long> {
    Optional<Assignment> findById(Long id);

    void deleteById(Long id);
}
