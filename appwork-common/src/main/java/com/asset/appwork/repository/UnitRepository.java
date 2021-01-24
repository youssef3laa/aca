package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends GenericRepository<Unit, Long> {
    Optional<Unit> findById(Long id);

    List<Unit> findAllByUnitCodeNotNull();
    List<Unit> findAllByUnitCodeNotNull(Pageable pageable);

    Optional<Unit> findByNameAndUnitCodeNotNull(String name);

    List<Unit> findByNameInAndUnitCodeNotNull(List<String> names);
    List<Unit> findByNameInAndUnitCodeNotNull(List<String> names, Pageable pageable);

    List<Unit> findByUnitTypeCode(String code);
    List<Unit> findByUnitTypeCode(String code, Pageable pageable);

    List<Unit> findByUnitTypeCodeIn(List<String> codes);
    List<Unit> findByUnitTypeCodeIn(List<String> codes, Pageable pageable);

    List<Unit> findByParent(Unit unit);
    List<Unit> findByParent(Unit unit, Pageable pageable);

    List<Unit> findByParentIn(List<Unit> units);
    List<Unit> findByParentIn(List<Unit> units, Pageable pageable);

    Optional<Unit> findByChild(Unit unit);

    List<Unit> findByChildIn(List<Unit> units);
    List<Unit> findByChildIn(List<Unit> units, Pageable pageable);

    Optional<Unit> findByGroup(Group group);

    void deleteById(Long id);
}
