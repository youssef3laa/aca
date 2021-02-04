package com.asset.appwork.repository;

import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends GenericRepository<Position, Long> {
    Optional<Position> findById(Long id);

    Optional<Position> findByName(String name);

    List<Position> findByUnit(Unit unit);
    Page<Position> findByUnit(Unit unit, Pageable pageable);

    List<Position> findAllByNameNotNull();
    Page<Position> findAllByNameNotNull(Pageable pageable);
}
