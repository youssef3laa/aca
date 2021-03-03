package com.asset.appwork.repository;

import com.asset.appwork.enums.GroupType;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GroupRepository extends GenericRepository<Group, Long> {
    Optional<Group> findById(Long id);

    Optional<Group> findByName(String name);

//    Optional<Group> findByName(String name);

    List<Group> findByNameIn(List<String> names);

    Page<Group> findByNameIn(List<String> names, Pageable pageable);

    Optional<Group> findByUnit_UnitTypeCodeAndUnit_UnitCodeAndType(String unitTypeCode, String unitCode, GroupType type);

    List<Group> findByUnit(Unit unit);

    Page<Group> findByUnit(Unit unit, Pageable pageable);

    List<Group> findByUnitIn(Set<Unit> units);

    Page<Group> findByUnitIn(Set<Unit> units, Pageable pageable);

    List<Group> findByUnitAndType(Unit unit, GroupType type);

    Page<Group> findByUnitAndType(Unit unit, GroupType type, Pageable pageable);

//    List<Group> findAll();
//
//    Page<Group> findAll(Pageable pageable);

    @Query("SELECT G FROM Group G WHERE G.name LIKE %:searchString% OR G.nameAr LIKE %:searchString% " +
            "OR G.groupCode LIKE %:searchString%")
    List<Group> findAllSearchable(String searchString);

    @Query("SELECT G FROM Group G WHERE G.name LIKE %:searchString% OR G.nameAr LIKE %:searchString% " +
            "OR G.groupCode LIKE %:searchString%")
    Page<Group> findAllSearchable(String searchString, Pageable pageable);

//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getGroupChildrenRecursivelyFilteredByUnitTypeCode(:groupCode_param, :unitTypeCode_param)}", nativeQuery = true)
//    List<Group> getGroupChildrenRecursivelyFilteredByUnitTypeCode(@Param("groupCode_param") String groupCode,
//                                                                  @Param("unitTypeCode_param") String unitTypeCode);
//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getGroupChildrenRecursivelyFilteredByUnitTypeCode(:groupCode_param, :unitTypeCode_param)}", nativeQuery = true)
//    Page<Group> getGroupChildrenRecursivelyFilteredByUnitTypeCode(@Param("groupCode_param") String groupCode,
//                                                                  @Param("unitTypeCode_param") String unitTypeCode,
//                                                                  Pageable pageable);
}
