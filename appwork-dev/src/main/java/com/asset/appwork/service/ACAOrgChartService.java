package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.*;
import com.asset.appwork.otds.Otds;
import com.asset.appwork.util.ACAAdapter;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ACAOrgChartService extends OrgChartService {

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
        String parentTypeCode = newData.split("-")[0];
        String parentCode = newData.split("-")[1];
        Unit createdUnit = super.createUnit(account, toUnitCreationString(unit));
        if(!isRootUnit)
            addSubUnitToUnit(account, getUnit(parentTypeCode, parentCode).getId(), createdUnit.getId());
        return createdUnit;
    }

    @Override
    public Unit createUnit(Account account, String props) throws AppworkException, JsonProcessingException {
        return createUnit(account, props, false);
    }

    public Unit renameUnit(Account account, String props) throws AppworkException, JsonProcessingException {
        Unit newUnit = fromACAUnitRenamingString(props);
        Unit unit = getUnit(newUnit.getUnitTypeCode(), newUnit.getDescription());
        newUnit.setDescription(null);

        // TODO: Check Cached result
        return updateUnit(account, unit.getId(), toUnitRenamingString(newUnit));
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

        addSubUnitToUnit(account, getUnit(newParentUnitTypeCode, newParentUnitCode).getId(),
                getUnit(oldUnit.getUnitTypeCode(), oldUnit.getUnitCode()).getId());

        unit.setUnitCode(newUnitCode);
        return updateUnit(account, oldUnit.getId(), toUnitChangingParentString(unit));
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
        Unit updatedUnit = updateUnit(account, oldUnit.getId(), toUnitParentAndChangingUnitTypeCodeString(unit));

        addSubUnitToUnit(account, getUnit(newParentUnitTypeCode, newParentUnitCode).getId(),
                getUnit(unit.getUnitTypeCode(), unit.getUnitCode()).getId());
        return updatedUnit;
    }

    public User getUserByDescription(String description) throws AppworkException {
        return userRepository.findByDescription(description).orElseThrow(
                () -> new AppworkException(String.format("Could not get User of Id: %s", description), ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Group getGroupByUnitTypeCodeAndUnitCodeAndIsHeadRoleAndIsViceRole(String unitTypeCode, String unitCode, Boolean isHeadRole, Boolean isViceRole) throws AppworkException{
        return groupRepository.findByUnit_UnitTypeCodeAndUnit_UnitCodeAndIsHeadRoleAndIsViceRole(unitTypeCode, unitCode, isHeadRole, isViceRole).orElseThrow(
                () -> new AppworkException("Could not get Group", ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Group getGroupByLevel(String unitTypeCode, String unitCode, String level) throws AppworkException {
        switch (level) {
            case "H":
                return getGroupByUnitTypeCodeAndUnitCodeAndIsHeadRoleAndIsViceRole(unitTypeCode, unitCode, true, false);
            case "V":
                return getGroupByUnitTypeCodeAndUnitCodeAndIsHeadRoleAndIsViceRole(unitTypeCode, unitCode, false, true);
            case "A":
                break;
            case "S":
                break;
            default:
                return getGroupByUnitTypeCodeAndUnitCodeAndIsHeadRoleAndIsViceRole(unitTypeCode, unitCode, false, false);
        }
        return getGroupByUnitTypeCodeAndUnitCodeAndIsHeadRoleAndIsViceRole(unitTypeCode, unitCode, false, false);
    }

    public User updateUser(Account account, String props, Boolean unitChanged, Boolean revokeTasks) throws AppworkException, JsonProcessingException {
        User user = fromACAUserUpdate(props);

        String newData = user.getPerson().getNotes();
        user.getPerson().setNotes(null);
        String newTypeCode = newData.split("-")[0];
        String newUnitTypeCode = newData.split("-")[1];
        String newUnitCode = newData.split("-")[2];

        User internalUser = getUserByDescription(user.getDescription());
        if(unitChanged)
            assignUserToGroup(account, internalUser.getUserId(),
                    getGroupByLevel(newUnitTypeCode, newUnitCode, newTypeCode).getGroupCode());

        List<Member.Values> memberValues = new ArrayList<>();
        List<String> titleValues = new ArrayList<>();
        titleValues.add(user.getPerson().getTitle());
        memberValues.add(new Member.Values("title", titleValues));
        List<String> displayNameValues = new ArrayList<>();
        displayNameValues.add(user.getDisplayName());
        memberValues.add(new Member.Values("displayName", displayNameValues));

        Member member = new Member(env.getProperty("otds.partition"), internalUser.getUserId(), memberValues);

        Otds otds = new Otds(account, SystemUtil.generateOtdsAPIBaseUrl(env), env.getProperty("otds.partition"));
        otds.updateUserByUserId(internalUser.getUserId(), member.toString());

        return getUserByUserId(internalUser.getUserId());
    }
}