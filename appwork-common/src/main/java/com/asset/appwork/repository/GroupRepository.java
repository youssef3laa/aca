package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GroupRepository extends GenericRepository<Group, Long> {
    Optional<Group> findByName(String name);

    Optional<Collection<Group>> findByNameIn(Collection<String> names);

    Optional<Collection<Group>> findByUnit(Unit unit);

    //TODO: Need Fixing?
    Optional<Collection<Group>> findByUnitIn(Set<Unit> units);
}
