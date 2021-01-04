package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "{call ACA_ORG_SP_getGroupByCodeAndDirection(:groupCode, :direction, :unitTypeCode)}", nativeQuery = true)
    Optional<Collection<Group>> getGroupByCodeAndDirection(@Param("groupCode") String groupCode,
                                                           @Param("direction") String direction,
                                                           @Param("unitTypeCode") String unitTypeCode);
}
