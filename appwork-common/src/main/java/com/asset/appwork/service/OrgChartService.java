package com.asset.appwork.service;

import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Group;
import com.asset.appwork.repository.GroupRepository;
import com.asset.appwork.repository.UnitRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class OrgChartService {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    GroupRepository groupRepository;

    public Optional<Group> getGroupParent(String code){
        Optional<Group>[] parent = new Optional[1];
        groupRepository.findByName(code).ifPresent(
                group -> unitRepository.findByGroup(group).ifPresent(
                        units -> unitRepository.findByChildIn(units).ifPresent(
                                parentUnits -> groupRepository.findByUnitIn(new HashSet<>(parentUnits)).ifPresent(
                                        groups -> {
                                             parent[0] = groups.stream().findFirst();
                                        }
                                )
                        )
                )
        );
        return parent[0];
    }
}
