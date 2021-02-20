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
    List<Lookup> findByCategory(String category);
    Optional<Lookup> findByCategoryAndKey(String category, String key);

    @Query("select L.category from Lookup L GROUP BY L.category")
    Page<String> findDistinctCategories(Pageable pageable);
}
