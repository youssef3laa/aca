package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findById(Long id);

//    List<User> findAll();
//    Page<User> findAll(Pageable pageable);

    @Query("SELECT U FROM User U WHERE U.name LIKE %:searchString% OR U.displayName LIKE %:searchString% " +
            "OR U.person.email LIKE %:searchString% OR U.person.phone LIKE %:searchString%")
    List<User> findAllSearchable(String searchString);

    @Query("SELECT U FROM User U WHERE U.name LIKE %:searchString% OR U.displayName LIKE %:searchString% " +
            "OR U.person.email LIKE %:searchString% OR U.person.phone LIKE %:searchString%")
    Page<User> findAllSearchable(String searchString, Pageable pageable);

    Optional<User> findByUserId(String userId);

    Optional<User> findByUserIdStartingWith(String userId);

    Optional<User> findByDescription(String description);

    List<User> findByGroup(Group group);

    Page<User> findByGroup(Group group, Pageable pageable);

    List<User> findByGroup_Unit(Unit unit);

    Page<User> findByGroup_Unit(Unit unit, Pageable pageable);

    void deleteById(Long id);
}
