package com.asset.appwork.service;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.GroupRepository;
import com.asset.appwork.repository.UnitRepository;
import com.asset.appwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgChartService {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;

    public Optional<Group> getGroupParent(String code) {
        return groupRepository.findByName(code)
                .flatMap(group -> unitRepository.findByChild(group.getUnit()))
                .flatMap(parentUnit -> groupRepository.findByUnit(parentUnit));
    }

    public Optional<User> getUserDetails(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserDetails(String userId) {
        return userRepository.findByUserId(userId);
    }
}
