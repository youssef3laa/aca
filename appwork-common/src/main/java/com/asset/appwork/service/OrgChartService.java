package com.asset.appwork.service;

import com.asset.appwork.model.Group;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import com.asset.appwork.repository.GroupRepository;
import com.asset.appwork.repository.PositionRepository;
import com.asset.appwork.repository.UnitRepository;
import com.asset.appwork.repository.UserRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrgChartService {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PositionRepository positionRepository;

    public Optional<Unit> getUnit(Long id) {
        return unitRepository.findById(id);
    }

    public void deleteUnit(Long id) {
        unitRepository.deleteById(id);
    }

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Optional<Position> getPosition(Long id) {
        return positionRepository.findById(id);
    }

    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public List<Unit> getUnitChildren(String code) {
        return unitRepository.findByNameAndUnitCodeNotNull(code)
                .map(unit -> unitRepository.findByParent(unit)).orElse(Collections.emptyList());
    }

    public Optional<Unit> getUnitParent(String code) {
        return unitRepository.findByNameAndUnitCodeNotNull(code)
                .flatMap(unit -> unitRepository.findByChild(unit));
    }

    public Optional<Group> getGroupParent(String code) {
        return groupRepository.findByNameAndGroupCodeNotNull(code)
                .flatMap(group -> unitRepository.findByChild(group.getUnit()))
                .flatMap(parentUnit -> groupRepository.findByUnit(parentUnit));
    }

    public List<Group> getGroupChildren(String code) {
        return groupRepository.findByNameAndGroupCodeNotNull(code)
                .map(group -> unitRepository.findByParent(group.getUnit()))
                .map(childUnits -> groupRepository.findByUnitIn(new HashSet<>(childUnits))).orElse(Collections.emptyList());
    }

    public List<Group> getGroupChildrenRecursivelyFilteredByUnitTypeCode(String code, String unitTypeCode) {
        return groupRepository.getGroupChildrenRecursivelyFilteredByUnitTypeCode(code, unitTypeCode);
    }

    public Optional<User> getUserDetails(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserDetails(String userId) {
        return userRepository.findByUserId(userId);
    }
}
