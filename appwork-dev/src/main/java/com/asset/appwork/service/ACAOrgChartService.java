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

    public Unit fromACAUnitString(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Unit.class, new ACAAdapter.UnitDeserializer());
        mapper.registerModule(module);
        return mapper.readValue(json, Unit.class);
    }

    public String toUnitString(Unit unit) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Unit.class, new ACAAdapter.UnitSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(unit);
    }

    public Unit getUnit(String unitCode, String unitTypeCode) throws AppworkException {
        return unitRepository.findByUnitCodeAndUnitTypeCode(unitCode, unitTypeCode).orElseThrow(
                () -> new AppworkException(String.format("Could not get Unit of UnitCode: %s and UnitTypeCode: %s", unitCode, unitTypeCode), ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    @Override
    public Unit createUnit(Account account, String props) throws AppworkException, JsonProcessingException {
        Unit unit = fromACAUnitString(props);
        String parentUnitCode = unit.getDescription();
        unit.setDescription(null);
        Unit createdUnit = super.createUnit(account, toUnitString(unit));
        addSubUnitToUnit(account, parentUnitCode, unit.getName());
        return createdUnit;
    }

    @Override
    public Unit updateUnit(Account account, Long id, String props) throws AppworkException {
        return super.updateUnit(account, id, props);
    }

    @Override
    public User updateUser(Account account, Long id, String props) throws AppworkException, JsonProcessingException {
        return super.updateUser(account, id, props);
    }
}