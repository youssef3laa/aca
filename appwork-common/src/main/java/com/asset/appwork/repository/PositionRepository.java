package com.asset.appwork.repository;

import com.asset.appwork.model.Position;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends GenericRepository<Position, Long> {
    Optional<Position> findById(Long id);

    List<Position>  findAll();
}
