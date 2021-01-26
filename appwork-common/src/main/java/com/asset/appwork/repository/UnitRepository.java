package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends GenericRepository<Unit, Long> {
    Optional<Unit> findById(Long id);

    List<Unit> findAllByUnitCodeNotNull();
    Page<Unit> findAllByUnitCodeNotNull(Pageable pageable);

    Optional<Unit> findByNameAndUnitCodeNotNull(String name);

    List<Unit> findByNameInAndUnitCodeNotNull(List<String> names);
    Page<Unit> findByNameInAndUnitCodeNotNull(List<String> names, Pageable pageable);

    List<Unit> findByUnitTypeCode(String code);
    Page<Unit> findByUnitTypeCode(String code, Pageable pageable);

    List<Unit> findByUnitTypeCodeIn(List<String> codes);
    Page<Unit> findByUnitTypeCodeIn(List<String> codes, Pageable pageable);

    List<Unit> findByParent(Unit unit);
    Page<Unit> findByParent(Unit unit, Pageable pageable);

    List<Unit> findByParentIn(List<Unit> units);
    Page<Unit> findByParentIn(List<Unit> units, Pageable pageable);

    Optional<Unit> findByChild(Unit unit);

    List<Unit> findByChildIn(List<Unit> units);
    Page<Unit> findByChildIn(List<Unit> units, Pageable pageable);

    Optional<Unit> findByGroup(Group group);

    void deleteById(Long id);

    @Query(value = "{call ACA_ORG_SP_getUnitChildrenRecursively(:unitCode)}", nativeQuery = true)
    List<Unit> getUnitChildrenRecursively(@Param("unitCode") String unitCode);
    @Query(value = "{call ACA_ORG_SP_getUnitChildrenRecursively(:unitCode)}", nativeQuery = true)
    Page<Unit> getUnitChildrenRecursively(@Param("unitCode") String unitCode,
                                                                  Pageable pageable);
}
