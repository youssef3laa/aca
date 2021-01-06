package com.asset.appwork.repository;

import com.asset.appwork.model.memoValues;
import com.asset.appwork.model.memorandum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemosRepository extends CrudRepository<memorandum, String> {
//    memoValues findByKey(String key);
    List<memorandum> findAll();
}