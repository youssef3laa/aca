package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends GenericRepository<Unit, Long> {
    Optional<Unit> findById(Long id);

//    List<Unit> findAll();
//
//    Page<Unit> findAll(Pageable pageable);

    @Query("SELECT U FROM Unit U WHERE U.name LIKE %:searchString% OR U.nameAr LIKE %:searchString% " +
            "OR U.unitTypeCode LIKE %:searchString% OR U.unitCode LIKE %:searchString%")
    List<Unit> findAllSearchable(String searchString);

    @Query("SELECT U FROM Unit U WHERE U.name LIKE %:searchString% OR U.nameAr LIKE %:searchString% " +
            "OR U.unitTypeCode LIKE %:searchString% OR U.unitCode LIKE %:searchString%")
    Page<Unit> findAllSearchable(String searchString, Pageable pageable);

    Optional<Unit> findByName(String name);

    List<Unit> findByNameIn(List<String> names);

    Page<Unit> findByNameIn(List<String> names, Pageable pageable);

    Optional<Unit> findByUnitTypeCodeAndUnitCode(String unitTypeCode, String unitCode);

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

//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getUnitChildrenRecursively(:unitCode_param)}", nativeQuery = true)
//    List<Unit> getUnitChildrenRecursively(@Param("unitCode_param") String unitCode);

//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getUnitChildrenRecursively(:unitCode_param)}", nativeQuery = true)
//    Page<Unit> getUnitChildrenRecursively(@Param("unitCode_param") String unitCode,
//                                          Pageable pageable);

//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getUnitChildrenRecursivelyFilteredByUnitTypeCode(:unitCode_param, :unitTypeCode_param)}", nativeQuery = true)
//    @Procedure(name = "Unit.getUnitChildrenRecursivelyFilteredByUnitTypeCode")
//    List<Unit> getUnitChildrenRecursivelyFilteredByUnitTypeCode(@Param("unitCode_param") String unitCode,
//                                                                @Param("unitTypeCode_param") String unitTypeCode);

//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getUnitChildrenRecursivelyFilteredByUnitTypeCode(:unitCode_param, :unitTypeCode_param)}", nativeQuery = true)
//    Page<Unit> getUnitChildrenRecursivelyFilteredByUnitTypeCode(@Param("unitCode_param") String unitCode,
//                                                                @Param("unitTypeCode_param") String unitTypeCode,
//                                                                Pageable pageable);

//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getUnitParentsRecursivelyFilteredByUnitTypeCode(:unitCode_param, :unitTypeCode_param)}", nativeQuery = true)
//    List<Unit> getUnitParentsRecursivelyFilteredByUnitTypeCode(@Param("unitCode_param") String unitCode,
//                                                               @Param("unitTypeCode_param") String unitTypeCode);

//    @Query(value = "{call APPWORKSDB.ACA_ORG_SP_getUnitParentsRecursivelyFilteredByUnitTypeCode(:unitCode_param, :unitTypeCode_param)}", nativeQuery = true)
//    Page<Unit> getUnitParentsRecursivelyFilteredByUnitTypeCode(@Param("unitCode_param") String unitCode,
//                                                               @Param("unitTypeCode_param") String unitTypeCode,
//                                                               Pageable pageable);
}
