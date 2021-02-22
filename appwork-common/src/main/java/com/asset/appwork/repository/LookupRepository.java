package com.asset.appwork.repository;

import com.asset.appwork.model.Lookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LookupRepository extends GenericRepository<Lookup, Long> {

    @Query("select L from Lookup L inner join Lookup parent on (L.categoryId = parent.Id) where parent.category = :category and L.key = :key ")
    Optional<Lookup> findByCategoryAndKey(String category, String key);

    @Query("select L from Lookup L inner join Lookup parent on (L.categoryId = parent.Id) where parent.category = :category and (L.arValue like %:search% or L.stringKey like %:search%)")
    Page<Lookup> findCategoryValues(String category, String search, Pageable pageable);

    Page<Lookup> findByTypeAndArValueContainsOrCategoryContains(Integer type, String arValue, String category, Pageable pageable);
}
