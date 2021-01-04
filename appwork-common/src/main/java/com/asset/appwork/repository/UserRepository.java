package com.asset.appwork.repository;

import com.asset.appwork.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
