package com.asset.appwork.repository;

import com.asset.appwork.model.Security;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityRepository extends GenericRepository<Security, Long> {

    Optional<Security> findByTarget(String target);

}
