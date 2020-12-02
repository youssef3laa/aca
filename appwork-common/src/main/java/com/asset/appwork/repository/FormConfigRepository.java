package com.asset.appwork.repository;

import com.asset.appwork.model.FormConfig;

import java.util.Optional;

public interface FormConfigRepository extends GenericRepository<FormConfig, Long> {
    Optional<FormConfig> findByKey(String key);
}
