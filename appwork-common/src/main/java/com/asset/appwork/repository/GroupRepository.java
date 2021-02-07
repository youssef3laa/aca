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
import java.util.Set;

@Repository
public interface GroupRepository extends GenericRepository<Group, Long> {
    Optional<Group> findById(Long id);

    Optional<Group> findByName(String name);

    Optional<Group> findByNameAndGroupCodeNotNull(String name);

    List<Group> findByNameInAndGroupCodeNotNull(List<String> names);
    Page<Group> findByNameInAndGroupCodeNotNull(List<String> names, Pageable pageable);

    List<Group> findByUnit(Unit unit);
    Page<Group> findByUnit(Unit unit, Pageable pageable);

    List<Group> findByUnitIn(Set<Unit> units);
    Page<Group> findByUnitIn(Set<Unit> units, Pageable pageable);

    List<Group> findAllByGroupCodeNotNull();
    Page<Group> findAllByGroupCodeNotNull(Pageable pageable);

    @Query(value = "{call ACA_ORG_SP_getGroupChildrenRecursivelyFilteredByUnitTypeCode(:groupCode, :unitTypeCode)}", nativeQuery = true)
    List<Group> getGroupChildrenRecursivelyFilteredByUnitTypeCode(@Param("groupCode") String groupCode,
                                                                  @Param("unitTypeCode") String unitTypeCode);
    @Query(value = "{call ACA_ORG_SP_getGroupChildrenRecursivelyFilteredByUnitTypeCode(:groupCode, :unitTypeCode)}", nativeQuery = true)
    Page<Group> getGroupChildrenRecursivelyFilteredByUnitTypeCode(@Param("groupCode") String groupCode,
                                                                  @Param("unitTypeCode") String unitTypeCode,
                                                                  Pageable pageable);
}
