package com.asset.appwork.repository;

import com.asset.appwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findById(Long id);

    List<User> findAll();
    Page<User> findAll(Pageable pageable);

    Optional<User> findByUserId(String userId);
}
