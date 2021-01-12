package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.Position;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import com.asset.appwork.otds.Otds;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.GroupRepository;
import com.asset.appwork.repository.PositionRepository;
import com.asset.appwork.repository.UnitRepository;
import com.asset.appwork.repository.UserRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    Environment env;

    public Optional<Unit> createUnit(Account account, String props) throws AppworkException {
        return unitRepository.findById(new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").create(props));
    }

    public Optional<Unit> getUnit(Long id) {
        return unitRepository.findById(id);
    }

    public Optional<Unit> updateUnit(Account account, Long id, String props) {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").update(id, props);
        return unitRepository.findById(id);
    }

    public void deleteUnit(Long id) throws AppworkException {
        try {
            unitRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AppworkException("No Unit of Id " + id.toString() + " exists", ResponseCode.DELETE_ENTITY_FAILURE);
        }
    }

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Optional<Position> createPosition(Account account, Long unitId, String props) throws AppworkException {
        return positionRepository.findById(new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").createChild(unitId, "Position", props));
    }

    public Optional<Position> getPosition(Long id) {
        return positionRepository.findById(id);
    }

    public Optional<Position> updatePosition(Account account, Long unitId, Long id, String props) {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").updateChild(unitId, "Position", id, props);
        return positionRepository.findById(id);
    }

    public void deletePosition(Long id) throws AppworkException {
        try {
            positionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AppworkException("No Position of Id " + id.toString() + " exists", ResponseCode.DELETE_ENTITY_FAILURE);
        }
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

    public Optional<Group> getGroup(Long id) {
        return groupRepository.findById(id);
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

    public Optional<Group> getGroup(String cn) {
        Pattern pattern = Pattern.compile("cn=(.*?),cn=organizational roles");
        Matcher matcher = pattern.matcher(cn);
        String name = "";
        if (matcher.find()) {
            name = matcher.group(1);
        }
        return groupRepository.findByNameAndGroupCodeNotNull(name);
    }

    public Optional<User> getUserDetails(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserDetails(String userId) {
        return userRepository.findByUserId(userId);
    }
}
