package com.asset.appwork.repository;

import com.asset.appwork.model.memoValues;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Bassel on 31/1/2021.
 */
@Repository
public interface MemoValuesRepository extends GenericRepository<memoValues, String> {
    List<memoValues> findByMemosId(String memosId);
    List<memoValues> findByMemosIdAndJsonKey(String memosId, String jsonKey);
    List<memoValues> findAll();
}