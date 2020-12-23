package com.asset.appwork.repository;

import com.asset.appwork.model.Group;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

//import java.util.Optional;

@Repository
public interface GroupRepository extends GenericRepository<Group, Long>{
    Group findByName(String name);
    List<Group> findByNameIn(Collection<String> name);
}
