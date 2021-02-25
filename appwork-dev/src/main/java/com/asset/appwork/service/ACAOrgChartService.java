package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.GroupType;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.model.Member;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import com.asset.appwork.otds.Otds;
import com.asset.appwork.repository.*;
import com.asset.appwork.util.ACAAdapter;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ACAOrgChartService {

    @Autowired
    OrgChartService orgChartService;
    @Autowired
    BaseIdentityRepository identityRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    PositionRepository positionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    Environment env;

    public Unit fromACAUnitCreationString(String json) throws JsonProcessingException, AppworkException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Unit.class, new ACAAdapter.UnitCreationDeserializer());
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, Unit.class);
        } catch (NullPointerException e) {
            throw new AppworkException("Unknown Properties in JSON", ResponseCode.BAD_REQUEST);
        }
    }

    public String toUnitCreationString(Unit unit) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Unit.class, new ACAAdapter.UnitCreationSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(unit);
    }

    public Unit fromACAUnitRenamingString(String json) throws JsonProcessingException, AppworkException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Unit.class, new ACAAdapter.UnitRenamingDeserializer());
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, Unit.class);
        } catch (NullPointerException e) {
            throw new AppworkException("Unknown Properties in JSON", ResponseCode.BAD_REQUEST);
        }
    }

    public String toUnitRenamingString(Unit unit) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Unit.class, new ACAAdapter.UnitRenamingSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(unit);
    }

    public Unit fromACAUnitChangingParentString(String json) throws JsonProcessingException, AppworkException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Unit.class, new ACAAdapter.UnitChangingParentDeserializer());
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, Unit.class);
        } catch (NullPointerException e) {
            throw new AppworkException("Unknown Properties in JSON", ResponseCode.BAD_REQUEST);
        }
    }

    public String toUnitChangingParentString(Unit unit) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Unit.class, new ACAAdapter.UnitChangingParentSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(unit);
    }

    public Unit fromACAUnitParentAndChangingUnitTypeCodeString(String json) throws JsonProcessingException, AppworkException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Unit.class, new ACAAdapter.UnitParentAndChangingUnitTypeCodeDeserializer());
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, Unit.class);
        } catch (NullPointerException e) {
            throw new AppworkException("Unknown Properties in JSON", ResponseCode.BAD_REQUEST);
        }
    }

    public String toUnitParentAndChangingUnitTypeCodeString(Unit unit) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Unit.class, new ACAAdapter.UnitParentAndChangingUnitTypeCodeSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(unit);
    }

    public User fromACAUserUpdate(String json) throws JsonProcessingException, AppworkException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(User.class, new ACAAdapter.UserUpdateDeserializer());
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, User.class);
        } catch (NullPointerException e) {
            throw new AppworkException("Unknown Properties in JSON", ResponseCode.BAD_REQUEST);
        }
    }

    public String toUserUpdate(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(User.class, new ACAAdapter.UserUpdateSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(user);
    }

    public Unit getUnit(String unitTypeCode, String unitCode) throws AppworkException {
        return unitRepository.findByUnitTypeCodeAndUnitCode(unitTypeCode, unitCode).orElseThrow(
                () -> new AppworkException(String.format("Could not get Unit of UnitCode: %s and UnitTypeCode: %s", unitCode, unitTypeCode), ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Unit createUnit(Account account, String props, Boolean isRootUnit) throws AppworkException, JsonProcessingException {
        Unit unit = fromACAUnitCreationString(props);
        String newData = unit.getDescription();
        unit.setDescription(null);
        Unit createdUnit = orgChartService.createUnit(account, toUnitCreationString(unit));
        if(!isRootUnit) {
            try {
                orgChartService.addSubUnitToUnit(account, getUnit(newData.split("-")[0], newData.split("-")[1]).getId(), createdUnit.getId());
            } catch (AppworkException e) {
                orgChartService.deleteUnit(account, createdUnit.getId());
                throw new AppworkException(e.getMessage(), e.getCode());
            }
        }

        Arrays.asList(GroupType.values()).forEach(groupType -> {
            try {
                orgChartService.createGroup(account, generateGroupByTypeAndUnit(groupType, createdUnit).toString());
            } catch (AppworkException | JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });

        return createdUnit;
    }

    public Unit renameUnit(Account account, String props) throws AppworkException, JsonProcessingException {
        Unit newUnit = fromACAUnitRenamingString(props);
        Unit unit = getUnit(newUnit.getUnitTypeCode(), newUnit.getDescription());
        newUnit.setDescription(null);

        // TODO: Check Cached result
        return orgChartService.updateUnit(account, unit.getId(), toUnitRenamingString(newUnit));
    }

    public Unit changeUnitParent(Account account, String props, Boolean changeTypeCode) throws JsonProcessingException, AppworkException {
        if(!changeTypeCode)
            return changeUnitParentOnly(account, props);
        return changeUnitParentAndTypeCode(account, props);
    }

    public Unit changeUnitParentOnly(Account account, String props) throws JsonProcessingException, AppworkException {
        Unit unit = fromACAUnitChangingParentString(props);
        Unit oldUnit = getUnit(unit.getUnitTypeCode(), unit.getUnitCode());
        String newData = unit.getDescription();
        unit.setDescription(null);
        String newParentUnitTypeCode = newData.split("-")[0];
        String newParentUnitCode = newData.split("-")[1];
        String newUnitCode = newData.split("-")[2];

        orgChartService.addSubUnitToUnit(account, getUnit(newParentUnitTypeCode, newParentUnitCode).getId(),
                getUnit(oldUnit.getUnitTypeCode(), oldUnit.getUnitCode()).getId());

        unit.setUnitCode(newUnitCode);
        return orgChartService.updateUnit(account, oldUnit.getId(), toUnitChangingParentString(unit));
    }

    public Unit changeUnitParentAndTypeCode(Account account, String props) throws JsonProcessingException, AppworkException {

        // TODO: Set Active with false and see if any groups needs updating

        Unit unit = fromACAUnitParentAndChangingUnitTypeCodeString(props);
        Unit oldUnit = getUnit(unit.getUnitTypeCode(), unit.getUnitCode());
        String newData = unit.getDescription();
        unit.setDescription(null);
        String newParentUnitTypeCode = newData.split("-")[0];
        String newParentUnitCode = newData.split("-")[1];
        String newUnitTypeCode = newData.split("-")[2];
        String newUnitCode = newData.split("-")[3];

        unit.setUnitTypeCode(newUnitTypeCode);
        unit.setUnitCode(newUnitCode);
        Unit updatedUnit = orgChartService.updateUnit(account, oldUnit.getId(), toUnitParentAndChangingUnitTypeCodeString(unit));

        orgChartService.addSubUnitToUnit(account, getUnit(newParentUnitTypeCode, newParentUnitCode).getId(),
                getUnit(unit.getUnitTypeCode(), unit.getUnitCode()).getId());
        return updatedUnit;
    }

    public User getUserByDescription(String description) throws AppworkException {
        return userRepository.findByDescription(description).orElseThrow(
                () -> new AppworkException(String.format("Could not get User of Id: %s", description), ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Group getGroupByUnitTypeCodeAndUnitCodeAndType(String unitTypeCode, String unitCode, GroupType type) throws AppworkException{
        return groupRepository.findByUnit_UnitTypeCodeAndUnit_UnitCodeAndType(unitTypeCode, unitCode, type).orElseThrow(
                () -> new AppworkException("Could not get Group", ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public GroupType getGroupTypeByLevel(String level) {
        switch (level.toUpperCase()) {
            case "H":
                return GroupType.HEAD;
            case "V":
                return GroupType.VICE;
            case "A":
                return GroupType.ASSISTANT;
            case "S":
                return GroupType.SECRETARY;
            default:
                return GroupType.MEMBER;
        }
    }

    public String getGroupLevelByType(GroupType type) {
        switch (type) {
            case HEAD:
                return "H";
            case VICE:
                return "V";
            case ASSISTANT:
                return "A";
            case SECRETARY:
                return "S";
            default:
                return "M";
        }
    }

    public Group generateGroupByTypeAndUnit(GroupType type, Unit unit) {
        Group group = new Group();
        group.setType(type);
        group.setName(getGroupLevelByType(type) + unit.getName());
        group.setGroupCode(getGroupLevelByType(type) + unit.getUnitCode());
        switch (type) {
            case HEAD:
                group.setNameAr("رئيس " + unit.getNameAr());
                break;
            case VICE:
                group.setNameAr("نائب رئيس " + unit.getNameAr());
                break;
            case ASSISTANT:
                group.setNameAr("مساعد رئيس " + unit.getNameAr());
                break;
            case SECRETARY:
                group.setNameAr("سكرتارية " + unit.getNameAr());
                break;
            default:
                group.setNameAr("عضو " + unit.getNameAr());
                break;
        }
        return group;
    }

    public Group getGroupByLevel(String unitTypeCode, String unitCode, String level) throws AppworkException {
        return getGroupByUnitTypeCodeAndUnitCodeAndType(unitTypeCode, unitCode, getGroupTypeByLevel(level));
    }

    public User updateUser(Account account, String props, Boolean unitChanged, Boolean revokeTasks) throws AppworkException, JsonProcessingException {
        User user = fromACAUserUpdate(props);

        String newData = user.getPerson().getNotes();
        user.getPerson().setNotes(null);

        User internalUser = getUserByDescription(user.getDescription());
        if(unitChanged) {
            orgChartService.assignUserToGroup(account, internalUser.getUserId(),
                    getGroupByLevel(
                            newData.split("-")[1], newData.split("-")[2], newData.split("-")[0]
                    ).getGroupCode());
        }

        List<Member.Values> memberValues = new ArrayList<>();
        List<String> titleValues = new ArrayList<>();
        titleValues.add(user.getPerson().getTitle());
        memberValues.add(new Member.Values("title", titleValues));
        List<String> displayNameValues = new ArrayList<>();
        displayNameValues.add(user.getDisplayName());
        memberValues.add(new Member.Values("displayName", displayNameValues));

        Member member = new Member(env.getProperty("otds.active-directory.partition"), null, memberValues);
        member.setDescription(null);

//        Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.active-directory.partition"));
//        otds.updateUserByUserId(internalUser.getUserId(), member.toString());

//      String userResponse = otds.getUserByUserId(internalUser.getUserId());
//        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(userResponse, Member.class);
//        System.out.println(userResponse);
        return orgChartService.getUserByUserId(internalUser.getUserId());
    }
}