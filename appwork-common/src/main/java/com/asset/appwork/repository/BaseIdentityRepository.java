package com.asset.appwork.repository;

import com.asset.appwork.model.BaseIdentity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseIdentityRepository extends GenericRepository<BaseIdentity, Long> {
    Optional<BaseIdentity> findByName(String name);
}
