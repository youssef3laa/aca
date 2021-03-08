package com.asset.appwork.repository;

import com.asset.appwork.model.Delegation;
import com.asset.appwork.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DelegationRepository extends GenericRepository<Delegation, Long> {
    Optional<Delegation> findById(Long id);

    Optional<Delegation> findByUserId(String userId);

    List<Delegation> findAll();
    Page<Delegation> findAll(Pageable pageable);

    @Query("SELECT D FROM Delegation D WHERE cast(D.from as string) LIKE %:searchString% OR cast(D.to as string) LIKE %:searchString% " +
            "OR D.role LIKE %:searchString% OR D.userId LIKE %:searchString% OR D.delegatedTo LIKE %:searchString%")
    List<Delegation> findAllSearchable(String searchString);

    @Query("SELECT D FROM Delegation D WHERE cast(D.from as string) LIKE %:searchString% OR cast(D.to as string) LIKE %:searchString% " +
            "OR D.role LIKE %:searchString% OR D.userId LIKE %:searchString% OR D.delegatedTo LIKE %:searchString%")
    Page<Delegation> findAllSearchable(String searchString, Pageable pageable);

    void deleteById(Long id);
}
