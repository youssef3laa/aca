package com.asset.appwork.repository;

import com.asset.appwork.model.AnnualGoals;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnualGoalsRepository extends GenericRepository<AnnualGoals, Long> {
    Page<AnnualGoals> findAll(Pageable pageable);

    @Query("select distinct(L.year) from AnnualGoals L")
    List<Integer> getAllYears();

   Page<AnnualGoals> getAnnualGoalsByYearEquals(Integer year,Pageable pageable);
}
