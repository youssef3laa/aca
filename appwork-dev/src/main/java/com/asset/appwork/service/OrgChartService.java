package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.*;
import com.asset.appwork.otds.Otds;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.*;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    AssignmentRepository assignmentRepository;

    @Autowired
    Environment env;

    public Unit createUnit(Account account, String props) throws AppworkException {
        return getUnit(new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").create(Unit.fromString(props).toPlatformString()));
    }

    public Unit getUnit(Long id) throws AppworkException {
        return unitRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Unit Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Unit getUnitByName(String code) throws AppworkException {
        return unitRepository.findByNameAndUnitCodeNotNull(code).orElseThrow(
                () -> new AppworkException("Could not get Unit Entity of name " + code, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Unit updateUnit(Account account, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").update(id, Unit.fromString(props).toPlatformString());
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
        return unitRepository.findAllByUnitCodeNotNull();
    }

    public void addSubUnitToUnit(Account account, Long id, Long subUnitId) {
        Member.TargetId targetId = new Member.TargetId(subUnitId);
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").addRelation(id, "SubUnits", Collections.singletonList(targetId).toString());
    }

    public void addSubUnitToUnit(Account account, String parentUnitCode, String subUnitCode) throws AppworkException {
        Long id = getUnitByName(parentUnitCode).getId();
        Long subUnitId = getUnitByName(subUnitCode).getId();

        addSubUnitToUnit(account, id, subUnitId);
    }

    public Position createPosition(Account account, Long unitId, String props) throws AppworkException {
        return getPosition(new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").createChild(unitId, "Position", Position.fromString(props).toPlatformString()));
    }

    public Position getPosition(Long id) throws AppworkException {
        return positionRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Position Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Position getPositionByName(String name) throws AppworkException {
        return positionRepository.findByName(name).orElseThrow(
                () -> new AppworkException("Could not get Position Entity of name " + name, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Position updatePosition(Account account, Long unitId, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "OrganizationalUnit").updateChild(unitId, "Position", id,
                Position.fromString(props).toPlatformString());
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

    public Assignment createAssignment(Account account, Long unitId, Long positionId, String props) throws AppworkException {
        return getAssignment(new Entity(account,
                SystemUtil.generateRestAPIBaseUrl(env, "OpenTextEntityIdentityComponents"),
                "OrganizationalUnit").createAddChildRelation(unitId, "Position", positionId,
                "ToAssignment", Assignment.fromString(props).toPlatformString()));
    }

    public Assignment getAssignment(Long id) throws AppworkException {
        return assignmentRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Assignment Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Assignment updateAssignment(Account account, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "OpenTextEntityIdentityComponents"),
                "Assignment").update(id, Assignment.fromString(props).toPlatformString());
        return getAssignment(id);
    }

    public void deleteAssignment(Long id) throws AppworkException {
        // TODO: Find solution to the Transaction Exception Roll back overriding AppworkException
        try {
            assignmentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AppworkException("No Assignment of Id " + id.toString() + " exists", ResponseCode.DELETE_ENTITY_FAILURE);
        }
    }

    public void addAssignmentToPersonRelation(Account account, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "OpenTextEntityIdentityComponents"),
                "Assignment").addRelation(id, "toPersonToOne", props);
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

    public Group createGroup(Account account, String props) throws AppworkException, JsonProcessingException {
        Member member = new Member(env.getProperty("otds.partition"), SystemUtil.getJsonByPtrExpr(props, "/name"), Collections.emptyList());
        String createdGroupName = SystemUtil.getJsonByPtrExpr(
                new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"))
                        .createGroup(member.toString()),
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
                "Group").update(platformGroupPostUpdate.getId(), Group.fromString(props).toPlatformString());
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
        List<Group> groups = (List<Group>) getUserByUserId(account.getUsername() + "@" + env.getProperty("otds.partition")).getGroup();
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
        return groupRepository.findAllByGroupCodeNotNull();
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

        if (SystemUtil.isFieldInJson(props, "name")) {
            Member member = new Member(env.getProperty("otds.partition"), SystemUtil.getJsonByPtrExpr(props, "/name"), Collections.emptyList());
            new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"))
                    .updateGroupByGroupName(group.getName(), member.toString());
        }
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"),
                "Group").update(group.getId(), Group.fromString(props).toPlatformString());
        return getGroup(group.getId());
    }

    public void updateGroupUnitRelation(Account account, Long id, String props) throws AppworkException {
        new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, "AssetOrgACA"), "Group")
                .addRelation(id, "Unit", props);
        Group group = getGroup(id);
        Position position = new Position();
        position.setName(group.getName());
        position.setDescription(group.getDescription());
        position.setIsLead(group.getIsHeadRole());
        createPosition(account, Long.parseLong(SystemUtil.getJsonByPtrExpr(props, "/targetId")), position.toPlatformString());
    }

    public void updateGroupUnitRelationByCodes(Account account, String groupCode, String unitCode) throws AppworkException {
        Group group = getGroupByName(groupCode);
        Long groupId = group.getId();
        Long unitId = getUnitByName(unitCode).getId();
        String props = new Member.TargetId(unitId).toString();
        updateGroupUnitRelation(account, groupId, props);
    }

    public void deleteGroup(Account account, Long id) throws AppworkException {
        new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"))
                .deleteGroupByGroupName(getGroup(id).getName());
    }

    public User createUser(Account account, String props) throws AppworkException, JsonProcessingException {
        UserMember userMember = new ObjectMapper().readValue(props, UserMember.class);
        Member member = userMember.getMember(env.getProperty("otds.partition"));

        Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
        String createdUserResponse = otds.createUser(member.toString());
        String createdUserId = SystemUtil.getJsonByPtrExpr(createdUserResponse, "/id");

        otds.resetPassword(createdUserId, member.getPasswordResetJsonString(SystemUtil.getJsonByPtrExpr(props, "/password")));

        int counter = 0;
        do {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            counter++;
        } while (counter > 5 || userRepository.findByUserId(createdUserId).isEmpty());

        if (userRepository.findByUserId(createdUserId).isEmpty()) {
            throw new AppworkException("Could not get user with usedId " + createdUserId, ResponseCode.READ_ENTITY_FAILURE);
        }

        return userRepository.findByUserId(createdUserId).get();

    }

    public User getUser(Long id) throws AppworkException {
        return userRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get User of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public User getUserByUserId(String userId) throws AppworkException {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> new AppworkException("Could not get User of UserId " + userId, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public User getUserByUsername(String username) throws AppworkException {
        return userRepository.findByUserId(username + "@" + env.getProperty("otds.partition")).orElseThrow(
                () -> new AppworkException("Could not get User of username " + username, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public User updateUser(Account account, Long id, String props) throws AppworkException, JsonProcessingException {
        UserMember userMember = new ObjectMapper().readValue(props, UserMember.class);
        Member member = userMember.getMember(env.getProperty("otds.partition"));

        String userId = getUser(id).getUserId();

        Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
        otds.updateUserByUserId(userId, member.toString());

        otds.resetPassword(userId, member.getPasswordResetJsonString(SystemUtil.getJsonByPtrExpr(props, "/password")));

        return getUserByUserId(userId);

    }

    public void deleteUser(Account account, Long id) throws AppworkException {
        new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"))
                .deleteUserByUserId(getUser(id).getUserId());
    }

    public void assignUserToGroup(Account account, String userId, String groupCode) throws JsonProcessingException, AppworkException {
        Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
        otds.assignUserToGroupsByUserId(userId, new Member.StringList(
                Collections.singletonList(groupCode + "@" + env.getProperty("otds.partition"))
        ));

        User user = getUserByUserId(userId);
        Position position = getPositionByName(groupCode);
        Assignment assignment = new Assignment();
        assignment.setPrincipal(position.getIsLead());
        System.out.println(assignment.toString());
        System.out.println(position.toString());
        System.out.println(position.toPlatformString());
        assignment = createAssignment(account, position.getUnit().getId(), position.getId(), assignment.toPlatformString());

        addAssignmentToPersonRelation(account, assignment.getId(), new Member.TargetId(user.getPerson().getId()).toString());
    }
}
