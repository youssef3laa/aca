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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
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

    public Unit createUnit(Account account, String props) throws AppworkException {
        return getUnit(new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").create(props));
    }

    public Unit getUnit(Long id) throws AppworkException {
        return unitRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Unit Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Unit updateUnit(Account account, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").update(id, props);
        return getUnit(id);
    }

    public void deleteUnit(Long id) throws AppworkException {
        // TODO: Find solution to the Transaction Exception Roll back overriding AppworkException
        try {
            unitRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AppworkException("No Unit of Id " + id.toString() + " exists", ResponseCode.DELETE_ENTITY_FAILURE);
        }
    }

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Position createPosition(Account account, Long unitId, String props) throws AppworkException {
        return getPosition(new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").createChild(unitId, "Position", props));
    }

    public Position getPosition(Long id) throws AppworkException {
        return positionRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Position Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Position updatePosition(Account account, Long unitId, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").updateChild(unitId, "Position", id, props);
        return getPosition(id);
    }

    public void deletePosition(Long id) throws AppworkException {
        // TODO: Find solution to the Transaction Exception Roll back overriding AppworkException
        try {
            positionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AppworkException("No Position of Id " + id.toString() + " exists", ResponseCode.DELETE_ENTITY_FAILURE);
        }
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Unit getUnitParent(String code) throws AppworkException {
        return unitRepository.findByNameAndUnitCodeNotNull(code)
                .flatMap(unit -> unitRepository.findByChild(unit)).orElseThrow(
                        () -> new AppworkException("Could not get the Parent of Unit " + code, ResponseCode.INTERNAL_SERVER_ERROR)
                );
    }

    public List<Unit> getUnitChildren(String code) {
        return unitRepository.findByNameAndUnitCodeNotNull(code)
                .map(unit -> unitRepository.findByParent(unit)).orElse(Collections.emptyList());
    }

    public Group createGroup(Account account, String props) throws AppworkException {
        String createdGroupName = SystemUtil.getJsonByPtrExpr(
                new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"))
                        .createGroup(SystemUtil.getJsonObjectByPtrExpr(props, "/otdsGroup")),
                "/name");
        int counter = 0;
        do {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            counter++;
        } while (counter > 5 || groupRepository.findByName(createdGroupName).isEmpty());

        if (groupRepository.findByName(createdGroupName).isEmpty()) {
            throw new AppworkException("Could not update group with name " + createdGroupName, ResponseCode.UPDATE_ENTITY_FAILURE);
        }

        Group platformGroupPostUpdate = groupRepository.findByName(createdGroupName).get();

        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "Group").update(platformGroupPostUpdate.getId(), SystemUtil.getJsonObjectByPtrExpr(props, "/platformGroup"));
        return getGroup(platformGroupPostUpdate.getId());
    }

    public Group getGroup(Long id) throws AppworkException {
        return groupRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Group Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Group getGroupByName(String name) throws AppworkException {
        return groupRepository.findByNameAndGroupCodeNotNull(name).orElseThrow(
                () -> new AppworkException("Could not get Group Entity of name " + name, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public List<Group> getGroupsByNames(String names) throws AppworkException {
        return groupRepository.findByNameInAndGroupCodeNotNull(Arrays.asList(names.trim().split("\\s*,\\s*")));
    }

    public List<Group> getGroupsByUnitNames(String names) {
        return groupRepository.findByUnitIn(new HashSet<>(unitRepository.findByNameInAndUnitCodeNotNull(Arrays.asList(names.trim().split("\\s*,\\s*")))));
    }

    public List<Group> getGroupsByUnitTypeCode(String code) {
        return groupRepository.findByUnitIn(new HashSet<>(unitRepository.findByUnitTypeCode(code)));
    }

    public List<Group> getGroupsByUnitTypeCodes(String codes) {
        return groupRepository.findByUnitIn(new HashSet<>(unitRepository.findByUnitTypeCodeIn(Arrays.asList(codes.trim().split("\\s*,\\s*")))));
    }

    public Group getGroupParent(String code) throws AppworkException {
        return unitRepository.findByChild(getGroupByName(code).getUnit())
                .flatMap(parentUnit -> groupRepository.findByUnit(parentUnit)).orElseThrow(
                        () -> new AppworkException("Could not get the Parent of Group " + code, ResponseCode.INTERNAL_SERVER_ERROR)
                );
    }

    public List<Group> getGroupParentsOfLoggedInUser(Account account) throws AppworkException {
        List<Group> groups = (List<Group>) getUserDetails(account.getUsername() + "@" + env.getProperty("otds.partition")).getGroup();
        List<Group> parentGroups = new ArrayList<>();
        groups.forEach(group -> {
            try {
                parentGroups.add(getGroupParent(group.getName()));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return parentGroups;
    }

    public List<Group> getGroupChildren(String code) throws AppworkException {
        return groupRepository.findByUnitIn(new HashSet<>(unitRepository.findByParent(getGroupByName(code).getUnit())));
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> getGroupChildrenRecursivelyFilteredByUnitTypeCode(String code, String unitTypeCode) {
        return groupRepository.getGroupChildrenRecursivelyFilteredByUnitTypeCode(code, unitTypeCode);
    }

    public Group getGroupByCn(String cn) throws AppworkException {
        Pattern pattern = Pattern.compile("cn=(.*?),cn=organizational roles");
        Matcher matcher = pattern.matcher(cn);
        String name = "";
        if (matcher.find()) {
            name = matcher.group(1);
        }
        return getGroupByName(name);
    }

    public Group updateGroup(Account account, Long id, String props) throws AppworkException {
        Group group = getGroup(id);
        new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"))
                .updateGroupByGroupName(group.getName(), SystemUtil.getJsonObjectByPtrExpr(props, "/otdsGroup"));

        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "Group").update(group.getId(), SystemUtil.getJsonObjectByPtrExpr(props, "/platformGroup"));
        return getGroup(group.getId());
    }

    public Group updateGroupUnitRelation(Account account, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group")
                .addRelation(id, "Unit", props);
        return getGroup(id);
    }

    public void deleteGroup(Account account, Long id) throws AppworkException {
        new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"))
                .deleteGroupByGroupName(getGroup(id).getName());
    }

    public User getUserDetails(Long id) throws AppworkException {
        return userRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get User of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public User getUserDetails(String userId) throws AppworkException {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> new AppworkException("Could not get User of UserId " + userId, ResponseCode.READ_ENTITY_FAILURE)
        );
    }
}
