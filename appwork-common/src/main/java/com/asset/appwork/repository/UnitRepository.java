package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends GenericRepository<Unit, Long> {
    Optional<Unit> findById(Long id);

    List<Unit> findAll();

    Optional<Unit> findByName(String name);

    List<Unit> findByNameIn(List<String> names);

    List<Unit> findByParent(Unit unit);

    List<Unit> findByParentIn(List<Unit> units);

    Optional<Unit> findByChild(Unit unit);

    List<Unit> findByChildIn(List<Unit> units);

    Optional<Unit> findByGroup(Group group);

    void deleteById(Long id);
}
