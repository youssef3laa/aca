package com.asset.appwork.repository;

import com.asset.appwork.model.Memorandum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemosRepository extends GenericRepository<Memorandum, String> {

    List<Memorandum> findByRequestId(String requestId);

    List<Memorandum> findByJsonIdAndRequestId(String jsonId, String requestId);

    Memorandum findByNodeId(String nodeId);

    List<Memorandum> findAll();

    Page<Memorandum> findAll(Pageable pageable);

    Memorandum getTopByRequestIdOrderByIdDesc(String requestId);
}