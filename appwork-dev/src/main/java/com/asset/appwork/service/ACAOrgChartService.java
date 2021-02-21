package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
import com.asset.appwork.util.ACAAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public User updateUser(Account account, Long id, String props) throws AppworkException, JsonProcessingException {
        return super.updateUser(account, id, props);
    }
}