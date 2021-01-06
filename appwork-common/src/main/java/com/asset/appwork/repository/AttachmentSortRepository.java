package com.asset.appwork.repository;

import com.asset.appwork.model.AttachmentSort;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentSortRepository extends GenericRepository<AttachmentSort, Long> {

    List<AttachmentSort> getAllByBwsIdAndRequestEntityIdOrderByPositionAsc(@Param("bwsId") String bwsId, @Param("requestEntityId") String requestEntityId);
}

