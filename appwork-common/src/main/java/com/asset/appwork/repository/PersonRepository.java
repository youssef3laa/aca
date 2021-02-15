package com.asset.appwork.repository;

import com.asset.appwork.model.Person;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends GenericRepository<Person, Long> {
    Optional<Person> findById(Long id);

    void deleteById(Long id);
}
