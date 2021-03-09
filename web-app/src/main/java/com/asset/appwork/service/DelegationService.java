package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Delegation;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.DelegationRepository;
import com.asset.appwork.repository.LookupRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DelegationService {

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    Environment env;

    public Delegation createDelegation(Account account, String props) throws AppworkException, JsonProcessingException {
        return getDelegation(
                new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, env.getProperty("aca.general.solution")), "ACA_Entity_Delegation")
                        .create(props)
        );
    }

    public Delegation getDelegation(Long id) throws AppworkException {
        return delegationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Delegation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Delegation updateDelegation(Long id, Delegation newDelegation) throws AppworkException, JsonProcessingException {
        Delegation delegation = getDelegation(id);
        delegation.setFrom(newDelegation.getFrom());
        delegation.setTo(newDelegation.getTo());
        delegation.setRole(newDelegation.getRole());
        delegation.setUserId(newDelegation.getUserId());
        delegation.setDelegatedTo(newDelegation.getDelegatedTo());
        delegation.setActive(newDelegation.getActive());
        delegationRepository.save(delegation);
        return getDelegation(id);
    }

    public void deleteDelegation(Long id) {
        delegationRepository.deleteById(id);
    }

    public List<Delegation> getAllDelegation() {
        return delegationRepository.findAll();
    }

    public List<Delegation> getAllDelegationSearchable(String search) {
        return delegationRepository.findAll();
    }

    public Page<Delegation> getAllDelegation(int page, int size) {
        return delegationRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Delegation> getAllDelegationSearchable(String search, int page, int size) {
        return delegationRepository.findAll(PageRequest.of(page, size));
    }
}