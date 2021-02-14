package com.asset.appwork.repository;

import com.asset.appwork.model.Assignment;
import com.asset.appwork.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends GenericRepository<Assignment, Long> {
    Optional<Assignment> findById(Long id);

    List<Assignment> findByPerson(Person person);

    void deleteById(Long id);
}
