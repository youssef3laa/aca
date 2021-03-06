package com.asset.appwork.repository;

import com.asset.appwork.model.Signature;

import java.util.List;

public interface SignatureRepository extends GenericRepository<Signature, Long> {
    long countAllByIncomingEntityId(long incomingEntityId);
    List<Signature> findAllByIncomingEntityIdOrderBySignatureDateDescIdDesc(long incomingEntityId);
}
