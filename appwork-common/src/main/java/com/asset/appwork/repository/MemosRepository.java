package com.asset.appwork.repository;

import com.asset.appwork.model.Memorandum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemosRepository extends GenericRepository<Memorandum, String> {
    List<Memorandum> findByJsonId(String key);
    List<Memorandum> findByJsonIdAndRequestId(String jsonId, String requestId);
    List<Memorandum> findAll();
}