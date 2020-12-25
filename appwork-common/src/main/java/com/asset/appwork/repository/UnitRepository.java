package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UnitRepository extends GenericRepository<Unit, Long> {
    Optional<Unit> findByName(String name);

    Optional<Collection<Unit>> findByNameIn(Collection<String> names);

    Optional<Collection<Unit>> findByParent(Unit unit);

    Optional<Collection<Unit>> findByParentIn(Collection<Unit> unit);

    Optional<Collection<Unit>> findByChild(Unit unit);

    Optional<Collection<Unit>> findByChildIn(Collection<Unit> unit);

    Optional<Collection<Unit>> findByGroup(Group group);
}
