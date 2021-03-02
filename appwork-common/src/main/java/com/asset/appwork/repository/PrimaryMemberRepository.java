package com.asset.appwork.repository;

import com.asset.appwork.model.PrimaryMember;

import java.util.Optional;

public interface PrimaryMemberRepository extends GenericRepository<PrimaryMember, Long> {
    Optional<PrimaryMember> findByUserCNAndIncomingRegistration_id(String userCN, long incomingRegistrationId);
    Optional<PrimaryMember> findTopByIncomingRegistration_idOrderByIdDesc( long incomingRegistrationId);
}
